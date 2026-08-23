/*
 * This file is a part of Fomagram X
 */
package org.thunderdog.challegram.ui;

import android.content.Context;
import android.view.View;

import org.thunderdog.challegram.BuildConfig;
import org.thunderdog.challegram.R;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.v.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SettingsFomagramController extends RecyclerViewController<Void> implements View.OnClickListener {
  public SettingsFomagramController (Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override
  public int getId () {
    return R.id.controller_fomagram_settings;
  }

  @Override
  public CharSequence getName () {
    return "Настройки FomagramX";
  }

  private SettingsAdapter adapter;

  @Override
  protected void onCreateView (Context context, CustomRecyclerView recyclerView) {
    adapter = new SettingsAdapter(this);
    List<ListItem> items = new ArrayList<>();

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, "FomagramX"));
    items.add(new ListItem(ListItem.TYPE_SETTING, 0, R.drawable.baseline_info_24, "Версия: " + BuildConfig.ORIGINAL_VERSION_NAME));
    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, "Раздел персональных настроек FomagramX. Здесь будут появляться новые кастомные функции и твики."));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    adapter.setItems(items);
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void onClick (View v) {
    // Handling future settings item clicks
  }
}
