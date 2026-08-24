package org.thunderdog.challegram.unsorted;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import org.thunderdog.challegram.tool.Strings;
import org.thunderdog.challegram.tool.TGCountry;

import me.vkryl.core.StringUtils;

public final class FomagramSettings {
  // --- ТЕЛЕФОН ---
  public static final int PHONE_MASK_OFF = 0;
  public static final int PHONE_MASK_NUMBER_ONLY = 1;
  public static final int PHONE_MASK_FULL = 2;
  public static final int PHONE_MASK_HIDE_VIEW = 3;

  private static final String KEY_PHONE_MASK_MODE = "fomagram_phone_mask_mode";
  private static final String KEY_MASK_ALL_PHONES = "fomagram_mask_all_phones";
  private static final String KEY_FAKE_PHONE_COUNTRY = "fomagram_fake_phone_country";
  private static final String KEY_FAKE_PHONE_NUMBER = "fomagram_fake_phone_number";

  public static int getPhoneMaskMode () {
    return Settings.instance().getInt(KEY_PHONE_MASK_MODE, PHONE_MASK_OFF);
  }

  public static void setPhoneMaskMode (int mode) {
    Settings.instance().putInt(KEY_PHONE_MASK_MODE, mode);
  }

  public static boolean isHidePhoneView (boolean isOtherUser) {
    int mode = getPhoneMaskMode();
    if (mode == PHONE_MASK_HIDE_VIEW) {
      return !isOtherUser || isMaskAllPhones();
    }
    return false;
  }

  public static boolean isMaskAllPhones () {
    return Settings.instance().getBoolean(KEY_MASK_ALL_PHONES, false);
  }

  public static void setMaskAllPhones (boolean maskAll) {
    Settings.instance().putBoolean(KEY_MASK_ALL_PHONES, maskAll);
  }

  public static boolean hasFakePhone () {
    String number = Settings.instance().getString(KEY_FAKE_PHONE_NUMBER, "");
    return !StringUtils.isEmpty(number);
  }

  public static String getFakePhoneCountry () {
    return Settings.instance().getString(KEY_FAKE_PHONE_COUNTRY, "");
  }

  public static String getFakePhoneNumber () {
    return Settings.instance().getString(KEY_FAKE_PHONE_NUMBER, "");
  }

  public static String getFakePhoneFull () {
    String country = getFakePhoneCountry().replaceAll("[^0-9]", "");
    String number = getFakePhoneNumber().replaceAll("[^0-9]", "");
    if (StringUtils.isEmpty(number)) {
      return "";
    }
    return "+" + country + " " + number;
  }

  public static void setFakePhone (String country, String number) {
    Settings.instance().putString(KEY_FAKE_PHONE_COUNTRY, country != null ? country.trim() : "");
    Settings.instance().putString(KEY_FAKE_PHONE_NUMBER, number != null ? number.trim() : "");
  }

  public static void clearFakePhone () {
    setFakePhone("", "");
  }

  // --- НИКНЕЙМ ---
  public static final int USERNAME_MODE_OFF = 0;
  public static final int USERNAME_MODE_MASK = 1;
  public static final int USERNAME_MODE_HIDE_VIEW = 2;

  private static final String KEY_USERNAME_MODE = "fomagram_username_mode";
  private static final String KEY_MASK_ALL_USERNAMES = "fomagram_mask_all_usernames";
  private static final String KEY_MASK_CHANNELS_AND_GROUPS = "fomagram_mask_channels_and_groups";
  private static final String KEY_FAKE_USERNAME = "fomagram_fake_username";

  public static int getUsernameMode () {
    return Settings.instance().getInt(KEY_USERNAME_MODE, USERNAME_MODE_OFF);
  }

  public static void setUsernameMode (int mode) {
    Settings.instance().putInt(KEY_USERNAME_MODE, mode);
  }

  public static boolean isMaskAllUsernames () {
    return Settings.instance().getBoolean(KEY_MASK_ALL_USERNAMES, false);
  }

  public static void setMaskAllUsernames (boolean maskAll) {
    Settings.instance().putBoolean(KEY_MASK_ALL_USERNAMES, maskAll);
  }

  public static boolean isMaskChannelsAndGroups () {
    return Settings.instance().getBoolean(KEY_MASK_CHANNELS_AND_GROUPS, false);
  }

  public static void setMaskChannelsAndGroups (boolean mask) {
    Settings.instance().putBoolean(KEY_MASK_CHANNELS_AND_GROUPS, mask);
  }

  public static boolean hasFakeUsername () {
    return !StringUtils.isEmpty(getFakeUsername());
  }

  public static String getFakeUsername () {
    return Settings.instance().getString(KEY_FAKE_USERNAME, "");
  }

  public static void setFakeUsername (String fakeUsername) {
    if (fakeUsername != null) {
      fakeUsername = fakeUsername.trim();
      if (fakeUsername.startsWith("@")) {
        fakeUsername = fakeUsername.substring(1);
      }
    } else {
      fakeUsername = "";
    }
    Settings.instance().putString(KEY_FAKE_USERNAME, fakeUsername);
  }

  public static void clearFakeUsername () {
    setFakeUsername("");
  }

  public static boolean isHideUsernameView (boolean isOtherUser) {
    int mode = getUsernameMode();
    if (mode == USERNAME_MODE_HIDE_VIEW) {
      return !isOtherUser || isMaskAllUsernames();
    }
    return false;
  }

  public static boolean isHideChannelUsernameView () {
    int mode = getUsernameMode();
    return mode == USERNAME_MODE_HIDE_VIEW && isMaskChannelsAndGroups();
  }

  public static String formatUsername (@Nullable String rawUsername, boolean isOtherUser) {
    if (rawUsername == null) {
      rawUsername = "";
    }
    if (rawUsername.startsWith("@")) {
      rawUsername = rawUsername.substring(1);
    }
    if (!isOtherUser && hasFakeUsername()) {
      rawUsername = getFakeUsername();
    }
    if (StringUtils.isEmpty(rawUsername)) {
      return "";
    }
    int mode = getUsernameMode();
    boolean shouldMask = mode == USERNAME_MODE_MASK && (!isOtherUser || isMaskAllUsernames());
    if (shouldMask) {
      StringBuilder sb = new StringBuilder("@");
      for (int i = 0; i < rawUsername.length(); i++) {
        sb.append("*");
      }
      return sb.toString();
    }
    return "@" + rawUsername;
  }

  // --- БОКОВОЕ МЕНЮ ---
  private static final String KEY_DRAWER_HIDE_CONTACTS = "fomagram_drawer_hide_contacts";
  private static final String KEY_DRAWER_HIDE_CALLS = "fomagram_drawer_hide_calls";
  private static final String KEY_DRAWER_HIDE_SAVED_MESSAGES = "fomagram_drawer_hide_saved_messages";
  private static final String KEY_DRAWER_HIDE_INVITE = "fomagram_drawer_hide_invite";
  private static final String KEY_DRAWER_HIDE_HELP = "fomagram_drawer_hide_help";
  private static final String KEY_DRAWER_HIDE_NIGHT = "fomagram_drawer_hide_night";

  public static boolean isDrawerHideContacts () {
    return Settings.instance().getBoolean(KEY_DRAWER_HIDE_CONTACTS, false);
  }

  public static void setDrawerHideContacts (boolean hide) {
    Settings.instance().putBoolean(KEY_DRAWER_HIDE_CONTACTS, hide);
  }

  public static boolean isDrawerHideCalls () {
    return Settings.instance().getBoolean(KEY_DRAWER_HIDE_CALLS, false);
  }

  public static void setDrawerHideCalls (boolean hide) {
    Settings.instance().putBoolean(KEY_DRAWER_HIDE_CALLS, hide);
  }

  public static boolean isDrawerHideSavedMessages () {
    return Settings.instance().getBoolean(KEY_DRAWER_HIDE_SAVED_MESSAGES, false);
  }

  public static void setDrawerHideSavedMessages (boolean hide) {
    Settings.instance().putBoolean(KEY_DRAWER_HIDE_SAVED_MESSAGES, hide);
  }

  public static boolean isDrawerHideInvite () {
    return Settings.instance().getBoolean(KEY_DRAWER_HIDE_INVITE, false);
  }

  public static void setDrawerHideInvite (boolean hide) {
    Settings.instance().putBoolean(KEY_DRAWER_HIDE_INVITE, hide);
  }

  public static boolean isDrawerHideHelp () {
    return Settings.instance().getBoolean(KEY_DRAWER_HIDE_HELP, false);
  }

  public static void setDrawerHideHelp (boolean hide) {
    Settings.instance().putBoolean(KEY_DRAWER_HIDE_HELP, hide);
  }

  public static boolean isDrawerHideNight () {
    return Settings.instance().getBoolean(KEY_DRAWER_HIDE_NIGHT, false);
  }

  public static void setDrawerHideNight (boolean hide) {
    Settings.instance().putBoolean(KEY_DRAWER_HIDE_NIGHT, hide);
  }

  // --- ЧАТ (БЕТА) ---
  private static final String KEY_CHAT_IOS_STYLE = "fomagram_chat_ios_style";

  public static boolean isIosChatStyle () {
    return Settings.instance().getBoolean(KEY_CHAT_IOS_STYLE, false);
  }

  public static void setIosChatStyle (boolean iosStyle) {
    Settings.instance().putBoolean(KEY_CHAT_IOS_STYLE, iosStyle);
  }

  // --- ПЕРЕЗАПУСК ПРИЛОЖЕНИЯ ---
  public static void restartApp (Context context) {
    try {
      Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
      if (intent != null) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
      }
    } catch (Throwable ignored) {}
    System.exit(0);
  }

  // --- МАСКИРОВКА И ФОРМАТИРОВАНИЕ ТЕЛЕФОНА ---
  public static String formatPhoneNumber (@Nullable String rawPhone, boolean isOtherUser) {
    if (!isOtherUser && hasFakePhone()) {
      String fake = getFakePhoneFull();
      int mode = getPhoneMaskMode();
      if (mode == PHONE_MASK_HIDE_VIEW) {
        return "";
      }
      return maskPhone(fake, mode);
    }

    if (rawPhone == null || rawPhone.isEmpty()) {
      return "";
    }

    int mode = getPhoneMaskMode();
    if (isHidePhoneView(isOtherUser)) {
      return "";
    }

    String formatted = Strings.formatPhone(rawPhone);
    if (mode == PHONE_MASK_OFF || (isOtherUser && !isMaskAllPhones())) {
      return formatted;
    }

    return maskPhone(formatted, mode);
  }

  public static String maskPhone (String formattedPhone, int mode) {
    if (formattedPhone == null || formattedPhone.isEmpty() || mode == PHONE_MASK_OFF) {
      return formattedPhone;
    }
    if (mode == PHONE_MASK_HIDE_VIEW) {
      return "";
    }

    String clean = formattedPhone.trim();
    boolean hasPlus = clean.startsWith("+");
    String digitsOnly = clean.replaceAll("[^0-9]", "");
    if (digitsOnly.isEmpty()) {
      return formattedPhone;
    }

    if (mode == PHONE_MASK_FULL) {
      StringBuilder sb = new StringBuilder();
      if (hasPlus) sb.append("+");
      for (int i = 0; i < digitsOnly.length(); i++) {
        sb.append("*");
      }
      return sb.toString();
    }

    if (mode == PHONE_MASK_NUMBER_ONLY) {
      // 1. Check if string has space delimiter from standard formatter e.g. "+7 999 123" or "+888 0 123"
      int firstSpace = clean.indexOf(' ');
      if (firstSpace > 0 && firstSpace < clean.length() - 1) {
        String countryPart = clean.substring(0, firstSpace);
        String restDigits = clean.substring(firstSpace + 1).replaceAll("[^0-9]", "");
        StringBuilder sb = new StringBuilder(countryPart).append(" ");
        for (int i = 0; i < restDigits.length(); i++) {
          sb.append("*");
        }
        return sb.toString();
      }

      // 2. Direct check for Anonymous numbers (+888)
      if (digitsOnly.startsWith("888")) {
        String restDigits = digitsOnly.substring(3);
        StringBuilder sb = new StringBuilder(hasPlus ? "+888 " : "888 ");
        for (int i = 0; i < restDigits.length(); i++) {
          sb.append("*");
        }
        return sb.toString();
      }

      // 3. Match against country database
      String countryCode = null;
      try {
        String[][] list = TGCountry.instance().getAll();
        if (list != null) {
          int longestLen = 0;
          for (String[] country : list) {
            if (country != null && country.length > 0 && country[0] != null) {
              String code = country[0];
              if (digitsOnly.startsWith(code) && code.length() > longestLen) {
                countryCode = code;
                longestLen = code.length();
              }
            }
          }
        }
      } catch (Throwable ignored) {}

      if (countryCode != null && countryCode.length() < digitsOnly.length()) {
        String restDigits = digitsOnly.substring(countryCode.length());
        StringBuilder sb = new StringBuilder(hasPlus ? "+" : "").append(countryCode).append(" ");
        for (int i = 0; i < restDigits.length(); i++) {
          sb.append("*");
        }
        return sb.toString();
      }

      // 4. Default fallback: first 1-3 digits
      int countryLen = digitsOnly.length() > 10 ? digitsOnly.length() - 10 : (digitsOnly.length() > 7 ? 1 : 0);
      StringBuilder sb = new StringBuilder();
      if (hasPlus) sb.append("+");
      if (countryLen > 0) {
        sb.append(digitsOnly.substring(0, countryLen)).append(" ");
      }
      for (int i = countryLen; i < digitsOnly.length(); i++) {
        sb.append("*");
      }
      return sb.toString();
    }

    return formattedPhone;
  }
}
