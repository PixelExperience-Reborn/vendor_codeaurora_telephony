/*
 * Copyright (c) 2025 Qualcomm Innovation Center, Inc. All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause-Clear
 */

package org.codeaurora.ims;

import android.content.Context;
import android.os.RemoteException;
import android.view.Surface;
import org.codeaurora.ims.internal.IVideoCallProviderListener;
import org.codeaurora.ims.internal.IVideoCallProvider;
import org.codeaurora.ims.utils.QtiImsExtUtils;

import java.util.concurrent.Executor;

public abstract class VideoCallProviderBase {
    public final class VideoCallProviderBinder extends IVideoCallProvider.Stub {

        @Override
        public void setListener(IVideoCallProviderListener listener)
                throws RemoteException {
            QtiImsExtUtils.executeMethodAsync(() ->
                    VideoCallProviderBase.this.onSetListener(listener), "setListener", mExecutor,
                    QtiImsExtUtils.MODIFY_PHONE_STATE, mContext);
        }

        @Override
        public void removeListener(IVideoCallProviderListener listener)
                throws RemoteException {
            QtiImsExtUtils.executeMethodAsync(() ->
                    VideoCallProviderBase.this.onRemoveListener(listener),
                    "removeListener", mExecutor, QtiImsExtUtils.MODIFY_PHONE_STATE,
                    mContext);
        }

        @Override
        public void setDisplaySurface(Surface surface)
                throws RemoteException {
            QtiImsExtUtils.executeMethodAsync(() ->
                    VideoCallProviderBase.this.onSetDisplaySurface(surface),
                    "setDisplaySurface", mExecutor, QtiImsExtUtils.MODIFY_PHONE_STATE,
                    mContext);
        }

        @Override
        public void setPreviewSurface(Surface surface) throws RemoteException {
            QtiImsExtUtils.executeMethodAsync(() ->
                    VideoCallProviderBase.this.onSetPreviewSurface(surface),
                    "setPreviewSurface", mExecutor, QtiImsExtUtils.MODIFY_PHONE_STATE,
                    mContext);
        }

        @Override
        public void setCamera(String cameraId) throws RemoteException {
            QtiImsExtUtils.executeMethodAsync(() ->
                    VideoCallProviderBase.this.onSetCamera(cameraId),
                    "setCamera", mExecutor, QtiImsExtUtils.MODIFY_PHONE_STATE,
                    mContext);
        }

        @Override
        public void requestCameraCapabilities() throws RemoteException {
            QtiImsExtUtils.executeMethodAsync(() ->
                    VideoCallProviderBase.this.onRequestCameraCapabilities(),
                    "requestCameraCapabilities", mExecutor, QtiImsExtUtils.READ_PHONE_STATE,
                    mContext);
        }
    }

    private IVideoCallProvider mBinder;
    private Executor mExecutor;
    private Context mContext;

    public IVideoCallProvider getBinder() {
        if (mBinder == null) {
            mBinder = new VideoCallProviderBinder();
        }
        return mBinder;
    }

    public VideoCallProviderBase(Executor executor, Context context) {
        mExecutor = executor;
        mContext = context;
    }

    protected void onSetListener(IVideoCallProviderListener listener) {
        //no-op
    }

    protected void onRemoveListener(IVideoCallProviderListener listener) {
        //no-op
    }

    protected void onSetPreviewSurface(Surface surface) {
        //no-op
    }

    protected void onSetDisplaySurface(Surface surface) {
        //no-op
    }

    protected void onSetCamera(String cameraId) {
        //no-op
    }

    protected void onRequestCameraCapabilities() {
        //no-op
    }
}
