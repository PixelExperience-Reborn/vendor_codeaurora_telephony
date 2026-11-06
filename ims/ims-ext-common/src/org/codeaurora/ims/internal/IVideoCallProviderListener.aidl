/*
 * Copyright (c) 2025 Qualcomm Innovation Center, Inc. All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause-Clear
 */

package org.codeaurora.ims.internal;

import android.telecom.VideoProfile.CameraCapabilities;
/**
 * Used by client application to get the result from lower layer by
 * communicating with vendor.
 * {@hide}
 */
oneway interface IVideoCallProviderListener {
    oneway void changeCameraCapabilities(in CameraCapabilities cc);
    oneway void changePeerDimensions(int width, int height);
    oneway void handleCallSessionEvent(int event);
}
