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
import it.pokefundroid.pokedroid.utils.SharedPreferencesUtilities;
import it.pokefundroid.pokedroid.utils.StaticClass;
import it.pokefundroid.pokedroid.viewUtils.ImageAdapter;
import it.pokefundroid.pokedroid.viewUtils.ParcelableMonster;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
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
import android.widget.Checkable;
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

	private ParcelableMonster pm;
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

		setContentView(R.layout.exchange_activity);

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
		this.pm = (ParcelableMonster) extras.getParcelable(PASSED_MONSTER_KEY);
		try {
			mMyMonster = pm.toMonster();
			mMyMonsterName.setText(pm.getName());
			mMyMonsterLevel.setText(String.format(
					getString(R.string.pokemon_level), pm.getLevel()));
			String sexChar = Monster.getSexAsci(pm.getSex());
			mMyMonsterSex.setText(sexChar);
			mMyMonsterSex
					.setTextColor(StaticClass.getColorFromSexAsci(sexChar));
			mMyMonsterPicture.setImageBitmap(BitmapFactory
					.decodeStream(getAssets().open(
							ImageAdapter.getMonsterFilename(pm.getId()))));
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
		} else {
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
								+ pm.getName());
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								mDialog.show();
							}
						});
						ExchangeActivity.this.sendMessage(ExchangeProtocolUtils
								.createSendMessage(pm.toMonster()));
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
					if(mDialog!=null && mDialog.isShowing())
						mDialog.dismiss();
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
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

	private void showConfirmDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.title_confirm)
				.setMessage(R.string.dialog_sure)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String msg;
								try {
									mAccepts += 1;
									msg = ExchangeProtocolUtils
											.createAcceptMessage(mOpponentMonster
													.getId());
									sendMessage(msg);
									Log.i("exchange", "sent accept");
									checkAccepts();
									dialog.dismiss();
									mDialog = new ProgressDialog(
											ExchangeActivity.this);
									((ProgressDialog) mDialog)
											.setMessage("arrived"
													+ mOpponentMonster.getId());
									mDialog.show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						})
				.setNeutralButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
		mDialog = builder.create();
		mDialog.show();
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
					showConfirmDialog();
					break;
				default:
					Toast.makeText(getApplicationContext(),
							getString(R.string.error), Toast.LENGTH_SHORT)
							.show();
					this.finish();
					break;
				}
			} else if (type.equals(ExchangeProtocolUtils.ACCEPT_COMMAND)) {
				mAccepts += 1;
				switch (mStatus) {
				case ACCEPT:

					if (mOpponentMonster != null) {
						if (ExchangeProtocolUtils.verifyAcceptMessage(
								new JSONObject(readMessage),
								Integer.parseInt(pm.getId()))) {
							// ((ProgressDialog) mDialog).setMessage("accepted"
							// + pm.getId());
							// mDialog.show();

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
				// case SEND:
				// if (mAccepts == 2) {
				// mStatus = STATUS.ACK;
				// sendMessage(ExchangeProtocolUtils
				// .createACKMessage(mOpponentMonster.getId()));
				// }
				// break;
				case ACK:
					mACKs += 1;
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

	private void checkAcks(String readMessage) throws NumberFormatException, JSONException {
		if (mACKs >= 2)
			// TODO do operations
			mDialog.dismiss();

			if (ExchangeProtocolUtils.verifyACKMessage(new JSONObject(
					readMessage), Integer.parseInt(pm.getId()))) {
				// Toast.makeText(this, "Faccio le operazioni",
				// Toast.LENGTH_SHORT).show();
				mMyMonster.removeFromDatabase();
				mOpponentMonster.saveOnDatabase();
				mStatus = STATUS.SEND;
				mChatService.stop();
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
			sendMessage(ExchangeProtocolUtils
					.createACKMessage(mOpponentMonster.getId()));
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