/*
 * Copyright 2019 The Android Open Source Project
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

package com.android.car.ui.recyclerview;

import static com.google.common.truth.Truth.assertThat;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;

import com.android.car.ui.CarUiRobolectricTestRunner;
import com.android.car.ui.R;
import com.android.car.ui.TestConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(CarUiRobolectricTestRunner.class)
@Config(manifest = TestConfig.MANIFEST_PATH, sdk = TestConfig.SDK_VERSION)
public class CarUiListItemTest {

    private CarUiRecyclerView mListView;
    private Context mContext;

    @Mock
    CarUiListItem.OnCheckedChangedListener mOnCheckedChangedListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mContext = RuntimeEnvironment.application;
        mListView = new CarUiRecyclerView(mContext);
    }

    private CarUiListItemAdapter.ViewHolder getViewHolderAtPosition(int position) {
        return (CarUiListItemAdapter.ViewHolder) mListView.findViewHolderForAdapterPosition(
                position);
    }

    private void updateRecyclerViewAdapter(CarUiListItemAdapter adapter) {
        mListView.setAdapter(adapter);

        // Force CarUiRecyclerView and the nested RecyclerView to be laid out.
        mListView.measure(0, 0);
        mListView.layout(0, 0, 100, 10000);

        if (mListView.mNestedRecyclerView != null) {
            mListView.mNestedRecyclerView.measure(0, 0);
            mListView.mNestedRecyclerView.layout(0, 0, 100, 10000);
        }

        // Required to init nested RecyclerView
        mListView.getViewTreeObserver().dispatchOnGlobalLayout();
    }

    @Test
    public void testItemVisibility_withTitle() {
        List<CarUiListItem> items = new ArrayList<>();

        CarUiListItem item = new CarUiListItem();
        item.setTitle("Test title");
        items.add(item);

        updateRecyclerViewAdapter(new CarUiListItemAdapter(items));

        assertThat(getViewHolderAtPosition(0).getTitle().getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getBody().getVisibility()).isNotEqualTo(View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getIconContainer().getVisibility()).isNotEqualTo(
                View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getActionContainer().getVisibility()).isNotEqualTo(
                View.VISIBLE);
    }

    @Test
    public void testItemVisibility_withTitle_withBody() {
        List<CarUiListItem> items = new ArrayList<>();

        CarUiListItem item = new CarUiListItem();
        item.setTitle("Test title");
        item.setBody("Test body");
        items.add(item);

        updateRecyclerViewAdapter(new CarUiListItemAdapter(items));

        assertThat(getViewHolderAtPosition(0).getTitle().getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getBody().getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getIconContainer().getVisibility()).isNotEqualTo(
                View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getActionContainer().getVisibility()).isNotEqualTo(
                View.VISIBLE);
    }

    @Test
    public void testItemVisibility_withTitle_withIcon() {
        List<CarUiListItem> items = new ArrayList<>();

        CarUiListItem item = new CarUiListItem();
        item.setTitle("Test title");
        item.setIcon(mContext.getDrawable(R.drawable.car_ui_icon_close));
        items.add(item);

        updateRecyclerViewAdapter(new CarUiListItemAdapter(items));

        assertThat(getViewHolderAtPosition(0).getTitle().getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getBody().getVisibility()).isNotEqualTo(View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getIconContainer().getVisibility()).isEqualTo(
                View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getIcon().getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getActionContainer().getVisibility()).isNotEqualTo(
                View.VISIBLE);
    }

    @Test
    public void testItemVisibility_withTitle_withCheckbox() {
        List<CarUiListItem> items = new ArrayList<>();

        CarUiListItem item = new CarUiListItem();
        item.setTitle("Test title");
        item.setAction(CarUiListItem.Action.CHECK_BOX);
        items.add(item);

        updateRecyclerViewAdapter(new CarUiListItemAdapter(items));

        assertThat(getViewHolderAtPosition(0).getTitle().getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getBody().getVisibility()).isNotEqualTo(View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getIconContainer().getVisibility()).isNotEqualTo(
                View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getActionContainer().getVisibility()).isEqualTo(
                View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getSwitch().getVisibility()).isNotEqualTo(
                View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getCheckBox().getVisibility()).isEqualTo(
                View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getCheckBox().isChecked()).isEqualTo(false);
    }

    @Test
    public void testItemVisibility_withTitle_withBody_withSwitch() {
        List<CarUiListItem> items = new ArrayList<>();

        CarUiListItem item = new CarUiListItem();
        item.setTitle("Test title");
        item.setBody("Body text");
        item.setAction(CarUiListItem.Action.SWITCH);
        items.add(item);

        updateRecyclerViewAdapter(new CarUiListItemAdapter(items));

        assertThat(getViewHolderAtPosition(0).getTitle().getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getBody().getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getIconContainer().getVisibility()).isNotEqualTo(
                View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getActionContainer().getVisibility()).isEqualTo(
                View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getSwitch().getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(getViewHolderAtPosition(0).getSwitch().isChecked()).isEqualTo(false);
        assertThat(getViewHolderAtPosition(0).getCheckBox().getVisibility()).isNotEqualTo(
                View.VISIBLE);
    }

    @Test
    public void testCheckedState_switch() {
        List<CarUiListItem> items = new ArrayList<>();

        CarUiListItem item = new CarUiListItem();
        item.setTitle("Test title");
        item.setChecked(true);
        item.setOnCheckedChangedListener(mOnCheckedChangedListener);
        item.setAction(CarUiListItem.Action.SWITCH);
        items.add(item);

        updateRecyclerViewAdapter(new CarUiListItemAdapter(items));

        Switch switchWidget = getViewHolderAtPosition(0).getSwitch();

        assertThat(switchWidget.isChecked()).isEqualTo(true);
        switchWidget.performClick();
        assertThat(switchWidget.isChecked()).isEqualTo(false);
        verify(mOnCheckedChangedListener, times(1)).onCheckedChanged(false);
    }

    @Test
    public void testCheckedState_checkbox() {
        List<CarUiListItem> items = new ArrayList<>();

        CarUiListItem item = new CarUiListItem();
        item.setTitle("Test title");
        item.setAction(CarUiListItem.Action.CHECK_BOX);
        item.setOnCheckedChangedListener(mOnCheckedChangedListener);
        items.add(item);

        updateRecyclerViewAdapter(new CarUiListItemAdapter(items));

        CheckBox checkBox = getViewHolderAtPosition(0).getCheckBox();

        assertThat(checkBox.isChecked()).isEqualTo(false);
        checkBox.performClick();
        assertThat(checkBox.isChecked()).isEqualTo(true);
        verify(mOnCheckedChangedListener, times(1)).onCheckedChanged(true);
    }
}