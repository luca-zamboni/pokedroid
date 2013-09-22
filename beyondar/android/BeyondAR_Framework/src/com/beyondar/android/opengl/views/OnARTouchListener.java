/*
 * Copyright (C) 2013 BeyondAR
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
package com.beyondar.android.opengl.views;

import android.view.MotionEvent;

public interface OnARTouchListener {

	/**
	 * Use
	 * {@link BeyondarGLSurfaceView#getARObjectOnScreenCoordinates(float, float)}
	 * to get the object touched:<br>
	 * <pre>
	 * {@code
	 * float x = event.getX();
	 * float y = event.getY();
	 * ArrayList<BeyondarObject> geoObjects = new ArrayList<BeyondarObject>();
	 * beyondarView.getARObjectOnScreenCoordinates(x, y, geoObjects);
	 * ...
	 * Now we iterate the ArrayList. The first element will be the closest one to the user
	 * ...
	 * }
	 * </pre>
	 * 
	 * @param event
	 * @param BeyondarView
	 */
	public void onTouchARView(MotionEvent event, BeyondarGLSurfaceView beyondarView);

}
