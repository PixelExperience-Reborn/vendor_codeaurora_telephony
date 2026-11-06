/*
 * Copyright (c) 2023,2025 Qualcomm Innovation Center, Inc. All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause-Clear.
 */

package org.codeaurora.ims;

import android.os.RemoteException;
import android.view.Surface;
import org.codeaurora.ims.internal.IImsArListener;
import org.codeaurora.ims.utils.QtiImsExtUtils;
import java.util.concurrent.Executor;

public abstract class ImsArListenerBase {

    private final class ArListener extends IImsArListener.Stub {

        public void onRecordingSurfaceChanged(int phoneId, Surface surface,
                int width, int height, String cameraId) throws RemoteException {
            QtiImsExtUtils.executeMethodAsync(() ->
                    ImsArListenerBase.this.onRecordingSurfaceChanged(phoneId, surface, width,
                    height, cameraId),
                    "onRecordingSurfaceChanged", mExecutor);
        }

        public void onRecorderFrameRateChanged(int phoneId, int rate, String cameraId)
                throws RemoteException {
            QtiImsExtUtils.executeMethodAsync(() ->
                    ImsArListenerBase.this.onRecorderFrameRateChanged(phoneId, rate, cameraId),
                    "onRecorderFrameRateChanged", mExecutor);
        }

        public void onRecordingEnabled(int phoneId, String cameraId) throws RemoteException {
            QtiImsExtUtils.executeMethodAsync(() ->
                    ImsArListenerBase.this.onRecordingEnabled(phoneId, cameraId),
                    "onRecordingEnabled", mExecutor);
        }

        public void onRecordingDisabled(int phoneId, String cameraId) throws RemoteException {
            QtiImsExtUtils.executeMethodAsync(() ->
                    ImsArListenerBase.this.onRecordingDisabled(phoneId, cameraId),
                    "onRecordingDisabled", mExecutor);
        }
    }

    private ArListener mListener;
    private Executor mExecutor;

    public IImsArListener getBinder() {
        if (mListener == null) {
            mListener = new ArListener();
        }
        return mListener;
    }

    public ImsArListenerBase() {
        mExecutor = Runnable::run;
    }

    public ImsArListenerBase(Executor executor) {
            mExecutor = executor;
    }

    protected void onRecordingSurfaceChanged(int phoneId, Surface surface,
            int width, int height, String cameraId){
        // no-op
    }

    protected void onRecorderFrameRateChanged(int phoneId, int rate, String cameraId) {
        // no-op
    }

    protected void onRecordingEnabled(int phoneId, String cameraId) {
        // no-op
    }

    protected void onRecordingDisabled(int phoneId, String cameraId) {
        // no-op
    }
}
