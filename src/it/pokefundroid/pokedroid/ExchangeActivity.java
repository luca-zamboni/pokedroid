/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.Monster;
import it.pokefundroid.pokedroid.utils.BluetoothChatService;
import it.pokefundroid.pokedroid.utils.ExchangeProtocolUtils;
import it.pokefundroid.pokedroid.utils.StaticClass;
import it.pokefundroid.pokedroid.viewUtils.ImageAdapter;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the main Activity that displays the current chat session.
 */
public class ExchangeActivity extends Activity {

	public enum STATUS {
		SEND, ACCEPT, ACK,
	}

	// Debugging
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothChatService mChatService = null;

	public static final String PASSED_MONSTER_KEY = "passedMonster";

	private Monster mMyMonster;
	private TextView mMyMonsterName;
	private ImageView mMyMonsterPicture;
	private TextView mMyMonsterSex;
	private TextView mMyMonsterLevel;

	private Monster mOpponentMonster;
	private TextView mOpponentMonsterName;
	private ImageView mOpponentMonsterPicture;
	private TextView mOpponentMonsterSex;
	private TextView mOpponentMonsterLevel;
	

	private STATUS mStatus = STATUS.SEND;
	private int mAccepts, mACKs;

	private Dialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_exchange);

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		Bundle extras = getIntent().getExtras();
		setupViews();
		setMyMonster(extras);
	}

	private void setupViews() {
		View myRow = findViewById(R.id.exchange_myPokemon);

		mMyMonsterName = (TextView) myRow.findViewById(R.id.pokemon_name);
		mMyMonsterPicture = (ImageView) myRow
				.findViewById(R.id.pokemon_picture);
		mMyMonsterSex = (TextView) myRow.findViewById(R.id.pokemon_sex);
		mMyMonsterLevel = (TextView) myRow.findViewById(R.id.pokemon_level);

		View opponentRow = findViewById(R.id.exchange_opponentPokemon);

		mOpponentMonsterName = (TextView) opponentRow
				.findViewById(R.id.pokemon_name);
		mOpponentMonsterPicture = (ImageView) opponentRow
				.findViewById(R.id.pokemon_picture);
		mOpponentMonsterSex = (TextView) opponentRow
				.findViewById(R.id.pokemon_sex);
		mOpponentMonsterLevel = (TextView) opponentRow
				.findViewById(R.id.pokemon_level);
	}

	private void setMyMonster(Bundle extras) {
		mMyMonster = (Monster) extras.getSerializable(PASSED_MONSTER_KEY);
		try {
			mMyMonsterName.setText(mMyMonster.getName());
			mMyMonsterLevel.setText(String.format(
					getString(R.string.pokemon_level), mMyMonster.getLevel()));
			String sexChar = Monster.getSexAsci(mMyMonster.getSex());
			mMyMonsterSex.setText(sexChar);
			mMyMonsterSex
					.setTextColor(StaticClass.getColorFromSexAsci(sexChar));
			mMyMonsterPicture.setImageBitmap(BitmapFactory
					.decodeStream(getAssets().open(
							ImageAdapter.getMonsterFilename(mMyMonster.getId()
									+ ""))));
		} catch (IOException e) {
			// TODO nothing for now
		}
	}

	private void setOpponentMonster() {
		try {
			mOpponentMonsterName.setText(mOpponentMonster.getName());
			mOpponentMonsterLevel.setText(String.format(
					getString(R.string.pokemon_level),
					mOpponentMonster.getLevel()));
			String sexChar = Monster.getSexAsci(mOpponentMonster.getSex());
			mOpponentMonsterSex.setText(sexChar);
			mOpponentMonsterSex.setTextColor(StaticClass
					.getColorFromSexAsci(sexChar));
			mOpponentMonsterPicture.setImageBitmap(BitmapFactory
					.decodeStream(getAssets().open(
							ImageAdapter.getMonsterFilename(""
									+ mOpponentMonster.getId()))));
			findViewById(R.id.exchange_opponentPokemon).setVisibility(
					View.VISIBLE);
		} catch (IOException e) {
			// TODO nothing for now
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mChatService == null)
				setupChat();
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		} else if(mBluetoothAdapter!=null && mBluetoothAdapter.isEnabled()) {
			mChatService = new BluetoothChatService(this, mHandler);
			mChatService.start();
		}
	}

	private void setupChat() {

		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatService(this, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if (mChatService != null)
			mChatService.stop();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mChatService != null)
			mChatService.stop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();
	}

	private void ensureDiscoverable() {
		if (D)
			Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            A string of text to send.
	 */
	private void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mChatService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
		}
	}

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					try {
						mDialog = new ProgressDialog(ExchangeActivity.this);
						((ProgressDialog) mDialog).setMessage("send"
								+ mMyMonster.getName());
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								mDialog.show();
							}
						});
						ExchangeActivity.this.sendMessage(ExchangeProtocolUtils
								.createSendMessage(mMyMonster));
					} catch (JSONException e) {
						Toast.makeText(getApplicationContext(),
								getString(R.string.error), Toast.LENGTH_SHORT)
								.show();
						ExchangeActivity.this.finish();
						mDialog.dismiss();
					}
					break;
				case BluetoothChatService.STATE_CONNECTING:
					// TODO CONNECTING
					mStatus = STATUS.SEND;
					break;
				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:
					// TODO TOAST NOT CONNECTED + EXIT
					mStatus = STATUS.SEND;
					if (mDialog != null && mDialog.isShowing())
						mDialog.dismiss();
					break;
				}
				break;
			case MESSAGE_WRITE:
				//byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				//String writeMessage = new String(writeBuf);
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				doAction(readMessage);
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// Get the device MAC address
				String address = data.getExtras().getString(
						DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				// Get the BLuetoothDevice object
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);
				// Attempt to connect to the device
				mChatService.connect(device);
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupChat();
			} else {
				// User did not enable Bluetooth or an error occured
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	protected void doAction(String readMessage) {
		try {
			String type = ExchangeProtocolUtils.getMessageType(readMessage);
			if (type.equals(ExchangeProtocolUtils.SEND_COMMAND)) {
				switch (mStatus) {
				case SEND:
					mOpponentMonster = ExchangeProtocolUtils
							.readSendJSON(new JSONObject(readMessage));
					setOpponentMonster();
					Log.i("exchange", "arrived: " + mOpponentMonster.getName());
					mDialog.dismiss();
					mStatus = STATUS.ACCEPT;
					toggleVisibility();
					break;
				default:
					Toast.makeText(getApplicationContext(),
							getString(R.string.error), Toast.LENGTH_SHORT)
							.show();
					this.finish();
					break;
				}
			} else if (type.equals(ExchangeProtocolUtils.ACCEPT_COMMAND)) {
				switch (mStatus) {
				case ACCEPT:

					if (mOpponentMonster != null) {
						if (ExchangeProtocolUtils.verifyAcceptMessage(
								new JSONObject(readMessage),mMyMonster.getId())) {

							mAccepts += 1;
							checkAccepts();
						} else {
							mStatus = STATUS.SEND;
							mChatService.stop();
							mDialog.dismiss();
							Toast.makeText(getApplicationContext(),
									"Don't accept", Toast.LENGTH_SHORT).show();
						}
					}
					break;
				case ACK:
					checkAcks(readMessage);
					break;
				default:
					Toast.makeText(getApplicationContext(),
							getString(R.string.error), Toast.LENGTH_SHORT)
							.show();
					mStatus = STATUS.SEND;
					mChatService.stop();
					break;
				}

			} else if (type.equals(ExchangeProtocolUtils.ACK_COMMAND)) {
				mACKs += 1;
				switch (mStatus) {
				case ACK:
					checkAcks(readMessage);
					break;
				case ACCEPT:
					mAccepts += 1;
					checkAccepts();
					break;
				default:
					Toast.makeText(getApplicationContext(),
							getString(R.string.error), Toast.LENGTH_SHORT)
							.show();
					mStatus = STATUS.SEND;
					mChatService.stop();
					break;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void checkAcks(String readMessage) throws NumberFormatException,
			JSONException {

		if (ExchangeProtocolUtils.verifyACKMessage(new JSONObject(readMessage),
				mMyMonster.getId())) {
			mACKs += 1;
		}
		if (mACKs >= 2) {
			mMyMonster.removeFromDatabase();
			mOpponentMonster.saveOnDatabase();
			mStatus = STATUS.SEND;
			mChatService.stop();
			mDialog.dismiss();
			this.finish();
		}
	}

	private void checkAccepts() throws JSONException {
		if (mAccepts >= 2) {
			mDialog.dismiss();
			mStatus = STATUS.ACK;
			// // Toast.makeText(this, "Faccio le operazioni",
			// // Toast.LENGTH_SHORT).show();
			// mMyMonster.removeFromDatabase();
			// mOpponentMonster.saveOnDatabase();
			sendMessage(ExchangeProtocolUtils.createACKMessage(mOpponentMonster
					.getId()));
		}
	}

	private void toggleVisibility() {
		View v = findViewById(R.id.accept_buttons);
		if (v.isShown()) {
			findViewById(R.id.exchange_label).setVisibility(View.INVISIBLE);
			v.setVisibility(View.INVISIBLE);
		} else {
			findViewById(R.id.exchange_label).setVisibility(View.VISIBLE);
			v.setVisibility(View.VISIBLE);
		}
	}


	public void exchange(View v) {
		toggleVisibility();
		if (v.getId() == R.id.exchange_yes_btn) {
			try {
				mAccepts += 1;
				String msg = ExchangeProtocolUtils
						.createAcceptMessage(mOpponentMonster.getId());
				sendMessage(msg);
				Log.i("exchange", "sent accept");
				checkAccepts();
				if (mDialog != null && mDialog.isShowing())
					mDialog.dismiss();
				mDialog = new ProgressDialog(ExchangeActivity.this);
				((ProgressDialog) mDialog)
						.setMessage(getString(R.string.wait_answer));
				mDialog.show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (v.getId() == R.id.exchange_no_btn) {
			try {
				String msg = ExchangeProtocolUtils.createAcceptMessage(-1);
				sendMessage(msg);
				if (mDialog != null && mDialog.isShowing())
					mDialog.dismiss();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.scan:
			// Launch the DeviceListActivity to see devices and do scan
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			return true;
		case R.id.discoverable:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		}
		return false;
	}

}