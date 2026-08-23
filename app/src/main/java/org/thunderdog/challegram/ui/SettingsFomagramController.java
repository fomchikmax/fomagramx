/*
 * This file is a part of Fomagram X
 */
package org.thunderdog.challegram.ui;

import android.content.Context;
import android.view.View;

import org.thunderdog.challegram.R;
import org.thunderdog.challegram.component.base.SettingView;
import org.thunderdog.challegram.navigation.SettingsWrapBuilder;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.unsorted.FomagramSettings;
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
    adapter = new SettingsAdapter(this) {
      @Override
      protected void setValuedSetting (ListItem item, SettingView view, boolean isUpdate) {
        final int itemId = item.getId();
        if (itemId == R.id.btn_fomagram_phone_mask) {
          view.setData(getPhoneMaskTitle(FomagramSettings.getPhoneMaskMode()));
        } else if (itemId == R.id.btn_fomagram_mask_all_phones) {
          view.getToggler().setRadioEnabled(FomagramSettings.isMaskAllPhones(), isUpdate);
        } else if (itemId == R.id.btn_fomagram_drawer_hide_contacts) {
          view.getToggler().setRadioEnabled(FomagramSettings.isDrawerHideContacts(), isUpdate);
        } else if (itemId == R.id.btn_fomagram_drawer_hide_calls) {
          view.getToggler().setRadioEnabled(FomagramSettings.isDrawerHideCalls(), isUpdate);
        } else if (itemId == R.id.btn_fomagram_drawer_hide_saved_messages) {
          view.getToggler().setRadioEnabled(FomagramSettings.isDrawerHideSavedMessages(), isUpdate);
        } else if (itemId == R.id.btn_fomagram_drawer_hide_invite) {
          view.getToggler().setRadioEnabled(FomagramSettings.isDrawerHideInvite(), isUpdate);
        } else if (itemId == R.id.btn_fomagram_drawer_hide_help) {
          view.getToggler().setRadioEnabled(FomagramSettings.isDrawerHideHelp(), isUpdate);
        } else if (itemId == R.id.btn_fomagram_drawer_hide_night) {
          view.getToggler().setRadioEnabled(FomagramSettings.isDrawerHideNight(), isUpdate);
        }
      }
    };

    List<ListItem> items = new ArrayList<>();

    // Секция: Телефон
    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, "Телефон"));
    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_fomagram_phone_mask, 0, "Скрытие номера"));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_fomagram_mask_all_phones, 0, "Скрывать у всех"));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, "Скрывает или маскирует звёздочками номера телефонов в профиле и боковом меню. «Скрывать у всех» применяет маскировку также к чужим профилям."));

    // Секция: Боковое меню
    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, "Боковое меню"));
    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_fomagram_drawer_hide_contacts, 0, "Скрыть «Контакты»"));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_fomagram_drawer_hide_calls, 0, "Скрыть «Звонки»"));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_fomagram_drawer_hide_saved_messages, 0, "Скрыть «Избранное»"));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_fomagram_drawer_hide_invite, 0, "Скрыть «Пригласить друзей»"));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_fomagram_drawer_hide_help, 0, "Скрыть «Помощь»"));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_fomagram_drawer_hide_night, 0, "Скрыть «Ночной режим»"));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, "Позволяет скрыть ненужные пункты из выезжающего бокового меню (шторки)."));

    adapter.setItems(items, false);
    recyclerView.setAdapter(adapter);
  }

  private static String getPhoneMaskTitle (int mode) {
    switch (mode) {
      case FomagramSettings.PHONE_MASK_NUMBER_ONLY:
        return "Скрывать только номер";
      case FomagramSettings.PHONE_MASK_FULL:
        return "Скрывать и номер, и страну";
      case FomagramSettings.PHONE_MASK_HIDE_VIEW:
        return "Скрыть плашку номера";
      case FomagramSettings.PHONE_MASK_OFF:
      default:
        return "Выкл";
    }
  }

  @Override
  public void onClick (View v) {
    final int viewId = v.getId();
    if (viewId == R.id.btn_fomagram_phone_mask) {
      showPhoneMaskOptions();
    } else if (viewId == R.id.btn_fomagram_mask_all_phones) {
      FomagramSettings.setMaskAllPhones(adapter.toggleView(v));
    } else if (viewId == R.id.btn_fomagram_drawer_hide_contacts) {
      FomagramSettings.setDrawerHideContacts(adapter.toggleView(v));
    } else if (viewId == R.id.btn_fomagram_drawer_hide_calls) {
      FomagramSettings.setDrawerHideCalls(adapter.toggleView(v));
    } else if (viewId == R.id.btn_fomagram_drawer_hide_saved_messages) {
      FomagramSettings.setDrawerHideSavedMessages(adapter.toggleView(v));
    } else if (viewId == R.id.btn_fomagram_drawer_hide_invite) {
      FomagramSettings.setDrawerHideInvite(adapter.toggleView(v));
    } else if (viewId == R.id.btn_fomagram_drawer_hide_help) {
      FomagramSettings.setDrawerHideHelp(adapter.toggleView(v));
    } else if (viewId == R.id.btn_fomagram_drawer_hide_night) {
      FomagramSettings.setDrawerHideNight(adapter.toggleView(v));
    }
  }

  private void showPhoneMaskOptions () {
    int current = FomagramSettings.getPhoneMaskMode();
    showSettings(new SettingsWrapBuilder(R.id.btn_fomagram_phone_mask).setRawItems(new ListItem[] {
      new ListItem(ListItem.TYPE_RADIO_OPTION, R.id.btn_fomagram_phone_mask_off, 0, "Выкл", R.id.btn_fomagram_phone_mask, current == FomagramSettings.PHONE_MASK_OFF),
      new ListItem(ListItem.TYPE_RADIO_OPTION, R.id.btn_fomagram_phone_mask_number, 0, "Скрывать только номер", R.id.btn_fomagram_phone_mask, current == FomagramSettings.PHONE_MASK_NUMBER_ONLY),
      new ListItem(ListItem.TYPE_RADIO_OPTION, R.id.btn_fomagram_phone_mask_full, 0, "Скрывать и номер, и страну", R.id.btn_fomagram_phone_mask, current == FomagramSettings.PHONE_MASK_FULL),
      new ListItem(ListItem.TYPE_RADIO_OPTION, R.id.btn_fomagram_phone_mask_hide_view, 0, "Скрыть плашку номера", R.id.btn_fomagram_phone_mask, current == FomagramSettings.PHONE_MASK_HIDE_VIEW),
    }).setAllowResize(false).addHeaderItem("Скрытие номера телефона").setIntDelegate((id, result) -> {
      int sel = result.get(R.id.btn_fomagram_phone_mask);
      int newMode = FomagramSettings.PHONE_MASK_OFF;
      if (sel == R.id.btn_fomagram_phone_mask_number) {
        newMode = FomagramSettings.PHONE_MASK_NUMBER_ONLY;
      } else if (sel == R.id.btn_fomagram_phone_mask_full) {
        newMode = FomagramSettings.PHONE_MASK_FULL;
      } else if (sel == R.id.btn_fomagram_phone_mask_hide_view) {
        newMode = FomagramSettings.PHONE_MASK_HIDE_VIEW;
      }
      FomagramSettings.setPhoneMaskMode(newMode);
      adapter.updateValuedSettingById(R.id.btn_fomagram_phone_mask);
    }));
  }
}
