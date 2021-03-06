/**
 * <pre>
 * Copyright 2015 Soulwolf Ching
 * Copyright 2015 The Android Open Source Project for android-dialog-builder
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </pre>
 */
package net.soulwolf.widget.dialogbuilder;

/**
 * author: Soulwolf Created on 2015/8/4 21:56.
 * email : Ching.Soulwolf@gmail.com
 *
 * Interface used to allow the creator of a dialog to run some code when the
 * dialog is shown.
 */
public interface OnShowListener {

    /**
     * This method will be invoked when the dialog is shown.
     *
     * @param dialog The dialog that was shown will be passed into the
     *            method.
     */
    public void onShow(MasterDialog dialog);

}
