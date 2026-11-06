/*
 * Copyright (c) 2025 Qualcomm Innovation Center, Inc. All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause-Clear
 */

package org.codeaurora.ims.internal;

import android.view.Surface;
import org.codeaurora.ims.internal.IVideoCallProviderListener;

/**
 * Used by client application to communicate with vendor code
 * {@hide}
 */
interface IVideoCallProvider {
    /**
     * Used by client to register call back listener with vendor for secondary video call provider
     *
     * @param listener, to receive dual video updates
     *
     */
    oneway void setListener(IVideoCallProviderListener listener);

    /**
     * Used by client to unregister call back listener with vendor for secondary video call provider
     *
     * @param listener, to unregister call back listener
     *
     */
    oneway void removeListener(IVideoCallProviderListener listener);

    /**
     * Used by clients to set display surface
     */
    oneway void setDisplaySurface(in Surface surface);

    /**
     * Used by clients to set preview surface
     */
    oneway void setPreviewSurface(in Surface surface);

    /**
     * Used by clients to set camera
     */
    oneway void setCamera(String cameraId);

    /**
     * Used by clients to request camera capabilities
     */
    oneway void requestCameraCapabilities();

}
