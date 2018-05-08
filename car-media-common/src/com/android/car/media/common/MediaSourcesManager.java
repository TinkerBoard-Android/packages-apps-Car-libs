/*
 * Copyright 2018 The Android Open Source Project
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

package com.android.car.media.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.service.media.MediaBrowserService;
import android.util.Log;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This manager provides access to the list of all possible media sources that can be selected
 * to be played.
 * <p>
 * It also provides means to set and retrieve the last played media source, in order to disambiguate
 * in cases where there is no media application currently playing.
 */
public class MediaSourcesManager {
    private static final String TAG = "MediaSourcesManager";
    private final Context mContext;

    /**
     * Creates a new instance of the manager for the given context
     */
    public MediaSourcesManager(Context context) {
        mContext = context;
    }

    /**
     * Obtains all available media sources in alphabetical order
     */
    public List<MediaSource> getAvailableMediaSources() {
        return getPackageNames().stream()
                .filter(packageName -> packageName != null)
                .map(packageName -> new MediaSource(mContext, packageName))
                .filter(mediaSource -> {
                    if (mediaSource.getName() == null) {
                        Log.w(TAG, "Found media source without name: "
                                + mediaSource.getPackageName());
                        return false;
                    }
                    return true;
                })
                .sorted(Comparator.comparing(mediaSource -> mediaSource.getName().toString()))
                .collect(Collectors.toList());
    }

    /**
     * Generates a set of all possible apps to choose from, including the ones that are just
     * media services.
     */
    private Set<String> getPackageNames() {
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_APP_MUSIC);

        Intent mediaIntent = new Intent();
        mediaIntent.setAction(MediaBrowserService.SERVICE_INTERFACE);

        List<ResolveInfo> availableActivities = packageManager.queryIntentActivities(intent, 0);
        List<ResolveInfo> mediaServices = packageManager.queryIntentServices(mediaIntent,
                PackageManager.GET_RESOLVED_FILTER);

        Set<String> apps = new HashSet<>();
        for (ResolveInfo info : mediaServices) {
            apps.add(info.serviceInfo.packageName);
        }
        for (ResolveInfo info : availableActivities) {
            apps.add(info.activityInfo.packageName);
        }
        return apps;
    }
}