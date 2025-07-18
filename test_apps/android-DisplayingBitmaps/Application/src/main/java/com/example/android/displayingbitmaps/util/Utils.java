/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.example.android.displayingbitmaps.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.StrictMode;

import com.example.android.displayingbitmaps.ui.ImageDetailActivity;
import com.example.android.displayingbitmaps.ui.ImageGridActivity;

/**
 * Class containing some static utility methods.
 */
public class Utils {
    private Utils() {
    }

    public static void enableStrictMode() {
        return;
        /*
        StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                new StrictMode.ThreadPolicy.Builder()
                        .detectAll()
                        .penaltyLog();
        StrictMode.VmPolicy.Builder vmPolicyBuilder =
                new StrictMode.VmPolicy.Builder()
                        .detectAll()
                        .penaltyLog();

        threadPolicyBuilder.penaltyFlashScreen();
        vmPolicyBuilder
                .setClassInstanceLimit(ImageGridActivity.class, 1)
                .setClassInstanceLimit(ImageDetailActivity.class, 1);
        StrictMode.setThreadPolicy(threadPolicyBuilder.build());
        StrictMode.setVmPolicy(vmPolicyBuilder.build());
         */
    }
}
