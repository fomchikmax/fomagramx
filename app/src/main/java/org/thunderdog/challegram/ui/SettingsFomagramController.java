/*
 * This file is a part of Fomagram X
 */
package org.thunderdog.challegram.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.thunderdog.challegram.R;
import org.thunderdog.challegram.component.base.SettingView;
import org.thunderdog.challegram.core.Lang;
import org.thunderdog.challegram.navigation.FrameLayoutFix;
import org.thunderdog.challegram.navigation.SettingsWrapBuilder;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.theme.ColorId;
import org.thunderdog.challegram.theme.Theme;
import org.thunderdog.challegram.tool.Paints;
import org.thunderdog.challegram.tool.Screen;
import org.thunderdog.challegram.tool.UI;
import org.thunderdog.challegram.unsorted.FomagramSettings;
import org.thunderdog.challegram.v.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.vkryl.android.ViewUtils;
import me.vkryl.core.StringUtils;

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
  private LinearLayout restartBanner;
  private Runnable hideRestartBannerRunnable;

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
        } else if (itemId == R.id.btn_fomagram_phone_fake) {
          view.setData(FomagramSettings.hasFakePhone() ? FomagramSettings.getFakePhoneFull() : "Выкл");
        } else if (itemId == R.id.btn_fomagram_username_mask) {
          view.setData(getUsernameMaskTitle(FomagramSettings.getUsernameMode()));
        } else if (itemId == R.id.btn_fomagram_mask_all_usernames) {
          view.getToggler().setRadioEnabled(FomagramSettings.isMaskAllUsernames(), isUpdate);
        } else if (itemId == R.id.btn_fomagram_username_fake) {
          view.setData(FomagramSettings.hasFakeUsername() ? "@" + FomagramSettings.getFakeUsername() : "Выкл");
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
        } else if (itemId == R.id.btn_fomagram_chat_ios_style) {
          view.getToggler().setRadioEnabled(FomagramSettings.isIosChatStyle(), isUpdate);
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
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_fomagram_phone_fake, 0, "Фейковый телефон"));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, "Маскирует или скрывает номер телефона. Фейковый номер заменяет отображаемый номер на указанный вами."));

    // Секция: Никнейм
    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, "Никнейм"));
    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_fomagram_username_mask, 0, "Скрытие ника"));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_fomagram_mask_all_usernames, 0, "Скрывать у других"));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
    items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_fomagram_username_fake, 0, "Фейковый ник"));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, "Маскирует или скрывает юзернейм в профиле. Фейковый ник заменяет ваш ник на кастомный."));

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

    // Секция: ЧАТ (БЕТА)
    items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, "ЧАТ (БЕТА)"));
    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_fomagram_chat_ios_style, 0, "Айось (Стиль iOS)"));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, "Перемещает аватарку чата в правый верхний угол (нажатие на неё открывает меню чата), а имя и статус центрируются."));

    adapter.setItems(items, false);
    recyclerView.setAdapter(adapter);

    setupRestartBanner(context);
  }

  private void setupRestartBanner (Context context) {
    restartBanner = new LinearLayout(context);
    restartBanner.setOrientation(LinearLayout.HORIZONTAL);
    restartBanner.setGravity(Gravity.CENTER_VERTICAL);
    restartBanner.setPadding(Screen.dp(16f), Screen.dp(10f), Screen.dp(16f), Screen.dp(10f));
    restartBanner.setBackground(Theme.fillingCard(Theme.getColor(ColorId.chatBackground)));

    TextView tvText = new TextView(context);
    tvText.setText("Требуется перезапуск");
    tvText.setTextColor(Theme.getColor(ColorId.textPrimary));
    tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
    tvText.setTypeface(Typeface.DEFAULT_BOLD);
    LinearLayout.LayoutParams lpText = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
    restartBanner.addView(tvText, lpText);

    TextView tvButton = new TextView(context);
    tvButton.setText("ПЕРЕЗАПУСК");
    tvButton.setTextColor(Theme.getColor(ColorId.textAction));
    tvButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    tvButton.setTypeface(Typeface.DEFAULT_BOLD);
    tvButton.setPadding(Screen.dp(8f), Screen.dp(6f), Screen.dp(8f), Screen.dp(6f));
    tvButton.setOnClickListener(v -> FomagramSettings.restartApp(context));
    restartBanner.addView(tvButton);

    FrameLayoutFix.LayoutParams params = FrameLayoutFix.newParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
    params.setMargins(Screen.dp(12f), 0, Screen.dp(12f), Screen.dp(16f));
    restartBanner.setLayoutParams(params);
    restartBanner.setVisibility(View.GONE);
    restartBanner.setAlpha(0f);
    restartBanner.setTranslationY(Screen.dp(60f));

    if (getContentView() instanceof ViewGroup) {
      ((ViewGroup) getContentView()).addView(restartBanner);
    }
  }

  public void showRestartBanner () {
    if (restartBanner == null) return;
    restartBanner.setVisibility(View.VISIBLE);
    restartBanner.animate().alpha(1f).translationY(0f).setDuration(250).start();

    if (hideRestartBannerRunnable != null) {
      restartBanner.removeCallbacks(hideRestartBannerRunnable);
    }
    hideRestartBannerRunnable = () -> {
      if (restartBanner != null) {
        restartBanner.animate().alpha(0f).translationY(Screen.dp(60f)).setDuration(250).withEndAction(() -> {
          if (restartBanner != null) {
            restartBanner.setVisibility(View.GONE);
          }
        }).start();
      }
    };
    restartBanner.postDelayed(hideRestartBannerRunnable, 5000);
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

  private static String getUsernameMaskTitle (int mode) {
    switch (mode) {
      case FomagramSettings.USERNAME_MODE_MASK:
        return "Скрывать ник";
      case FomagramSettings.USERNAME_MODE_HIDE_VIEW:
        return "Скрыть плашку с ником";
      case FomagramSettings.USERNAME_MODE_OFF:
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
      showRestartBanner();
    } else if (viewId == R.id.btn_fomagram_phone_fake) {
      showFakePhoneDialog();
    } else if (viewId == R.id.btn_fomagram_username_mask) {
      showUsernameMaskOptions();
    } else if (viewId == R.id.btn_fomagram_mask_all_usernames) {
      FomagramSettings.setMaskAllUsernames(adapter.toggleView(v));
      showRestartBanner();
    } else if (viewId == R.id.btn_fomagram_username_fake) {
      showFakeUsernameDialog();
    } else if (viewId == R.id.btn_fomagram_drawer_hide_contacts) {
      FomagramSettings.setDrawerHideContacts(adapter.toggleView(v));
      showRestartBanner();
    } else if (viewId == R.id.btn_fomagram_drawer_hide_calls) {
      FomagramSettings.setDrawerHideCalls(adapter.toggleView(v));
      showRestartBanner();
    } else if (viewId == R.id.btn_fomagram_drawer_hide_saved_messages) {
      FomagramSettings.setDrawerHideSavedMessages(adapter.toggleView(v));
      showRestartBanner();
    } else if (viewId == R.id.btn_fomagram_drawer_hide_invite) {
      FomagramSettings.setDrawerHideInvite(adapter.toggleView(v));
      showRestartBanner();
    } else if (viewId == R.id.btn_fomagram_drawer_hide_help) {
      FomagramSettings.setDrawerHideHelp(adapter.toggleView(v));
      showRestartBanner();
    } else if (viewId == R.id.btn_fomagram_drawer_hide_night) {
      FomagramSettings.setDrawerHideNight(adapter.toggleView(v));
      showRestartBanner();
    } else if (viewId == R.id.btn_fomagram_chat_ios_style) {
      FomagramSettings.setIosChatStyle(adapter.toggleView(v));
      showRestartBanner();
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
      showRestartBanner();
    }));
  }

  private void showUsernameMaskOptions () {
    int current = FomagramSettings.getUsernameMode();
    showSettings(new SettingsWrapBuilder(R.id.btn_fomagram_username_mask).setRawItems(new ListItem[] {
      new ListItem(ListItem.TYPE_RADIO_OPTION, R.id.btn_fomagram_username_mask_off, 0, "Выкл", R.id.btn_fomagram_username_mask, current == FomagramSettings.USERNAME_MODE_OFF),
      new ListItem(ListItem.TYPE_RADIO_OPTION, R.id.btn_fomagram_username_mask_stars, 0, "Скрывать ник", R.id.btn_fomagram_username_mask, current == FomagramSettings.USERNAME_MODE_MASK),
      new ListItem(ListItem.TYPE_RADIO_OPTION, R.id.btn_fomagram_username_mask_hide_view, 0, "Скрыть плашку с ником", R.id.btn_fomagram_username_mask, current == FomagramSettings.USERNAME_MODE_HIDE_VIEW),
    }).setAllowResize(false).addHeaderItem("Скрытие ника").setIntDelegate((id, result) -> {
      int sel = result.get(R.id.btn_fomagram_username_mask);
      int newMode = FomagramSettings.USERNAME_MODE_OFF;
      if (sel == R.id.btn_fomagram_username_mask_stars) {
        newMode = FomagramSettings.USERNAME_MODE_MASK;
      } else if (sel == R.id.btn_fomagram_username_mask_hide_view) {
        newMode = FomagramSettings.USERNAME_MODE_HIDE_VIEW;
      }
      FomagramSettings.setUsernameMode(newMode);
      adapter.updateValuedSettingById(R.id.btn_fomagram_username_mask);
      showRestartBanner();
    }));
  }

  private void showFakePhoneDialog () {
    LinearLayout layout = new LinearLayout(context);
    layout.setOrientation(LinearLayout.HORIZONTAL);
    layout.setPadding(Screen.dp(20f), Screen.dp(12f), Screen.dp(20f), Screen.dp(8f));

    EditText etCountry = new EditText(context);
    etCountry.setHint("7");
    etCountry.setInputType(InputType.TYPE_CLASS_PHONE);
    etCountry.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3) });
    etCountry.setText(FomagramSettings.getFakePhoneCountry());
    LinearLayout.LayoutParams lpCountry = new LinearLayout.LayoutParams(Screen.dp(60f), ViewGroup.LayoutParams.WRAP_CONTENT);
    lpCountry.setMargins(0, 0, Screen.dp(10f), 0);
    layout.addView(etCountry, lpCountry);

    EditText etNumber = new EditText(context);
    etNumber.setHint("9991234567");
    etNumber.setInputType(InputType.TYPE_CLASS_PHONE);
    etNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
    etNumber.setText(FomagramSettings.getFakePhoneNumber());
    LinearLayout.LayoutParams lpNumber = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
    layout.addView(etNumber, lpNumber);

    AlertDialog.Builder b = new AlertDialog.Builder(context, Theme.dialogTheme());
    b.setTitle("Фейковый телефон");
    b.setMessage("Введите код страны (1-3 цифры) и номер (5-12 цифр):");
    b.setView(layout);
    b.setPositiveButton("Сохранить", (dialog, which) -> {
      String country = etCountry.getText().toString().trim().replaceAll("[^0-9]", "");
      String number = etNumber.getText().toString().trim().replaceAll("[^0-9]", "");
      if (country.isEmpty() || number.length() < 5) {
        UI.showToast("Код страны: 1-3 цифр, номер: 5-12 цифр", Toast.LENGTH_SHORT);
        return;
      }
      FomagramSettings.setFakePhone(country, number);
      adapter.updateValuedSettingById(R.id.btn_fomagram_phone_fake);
      showRestartBanner();
    });
    b.setNeutralButton("Сбросить", (dialog, which) -> {
      FomagramSettings.clearFakePhone();
      adapter.updateValuedSettingById(R.id.btn_fomagram_phone_fake);
      showRestartBanner();
    });
    b.setNegativeButton(R.string.Cancel, null);
    showAlert(b);
  }

  private void showFakeUsernameDialog () {
    LinearLayout layout = new LinearLayout(context);
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setPadding(Screen.dp(20f), Screen.dp(12f), Screen.dp(20f), Screen.dp(8f));

    EditText etUsername = new EditText(context);
    etUsername.setHint("fomchik_bot");
    etUsername.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    etUsername.setText(FomagramSettings.getFakeUsername());
    layout.addView(etUsername, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    AlertDialog.Builder b = new AlertDialog.Builder(context, Theme.dialogTheme());
    b.setTitle("Фейковый никнейм");
    b.setMessage("Введите юзернейм без @:");
    b.setView(layout);
    b.setPositiveButton("Сохранить", (dialog, which) -> {
      String nick = etUsername.getText().toString().trim();
      if (nick.startsWith("@")) {
        nick = nick.substring(1);
      }
      FomagramSettings.setFakeUsername(nick);
      adapter.updateValuedSettingById(R.id.btn_fomagram_username_fake);
      showRestartBanner();
    });
    b.setNeutralButton("Сбросить", (dialog, which) -> {
      FomagramSettings.clearFakeUsername();
      adapter.updateValuedSettingById(R.id.btn_fomagram_username_fake);
      showRestartBanner();
    });
    b.setNegativeButton(R.string.Cancel, null);
    showAlert(b);
  }
}
