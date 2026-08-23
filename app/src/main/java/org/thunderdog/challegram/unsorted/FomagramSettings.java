package org.thunderdog.challegram.unsorted;

import androidx.annotation.Nullable;

import org.thunderdog.challegram.tool.Strings;

public final class FomagramSettings {
  public static final int PHONE_MASK_OFF = 0;
  public static final int PHONE_MASK_NUMBER_ONLY = 1;
  public static final int PHONE_MASK_FULL = 2;
  public static final int PHONE_MASK_HIDE_VIEW = 3;

  private static final String KEY_PHONE_MASK_MODE = "fomagram_phone_mask_mode";
  private static final String KEY_MASK_ALL_PHONES = "fomagram_mask_all_phones";

  private static final String KEY_DRAWER_HIDE_CONTACTS = "fomagram_drawer_hide_contacts";
  private static final String KEY_DRAWER_HIDE_CALLS = "fomagram_drawer_hide_calls";
  private static final String KEY_DRAWER_HIDE_SAVED_MESSAGES = "fomagram_drawer_hide_saved_messages";
  private static final String KEY_DRAWER_HIDE_INVITE = "fomagram_drawer_hide_invite";
  private static final String KEY_DRAWER_HIDE_HELP = "fomagram_drawer_hide_help";
  private static final String KEY_DRAWER_HIDE_NIGHT = "fomagram_drawer_hide_night";

  public static int getPhoneMaskMode () {
    return Settings.instance().getInt(KEY_PHONE_MASK_MODE, PHONE_MASK_OFF);
  }

  public static void setPhoneMaskMode (int mode) {
    Settings.instance().putInt(KEY_PHONE_MASK_MODE, mode);
  }

  public static boolean isHidePhoneView () {
    return getPhoneMaskMode() == PHONE_MASK_HIDE_VIEW;
  }

  public static boolean isMaskAllPhones () {
    return Settings.instance().getBoolean(KEY_MASK_ALL_PHONES, false);
  }

  public static void setMaskAllPhones (boolean maskAll) {
    Settings.instance().putBoolean(KEY_MASK_ALL_PHONES, maskAll);
  }

  // Drawer
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

  public static String formatPhoneNumber (@Nullable String rawPhone, boolean isOtherUser) {
    if (rawPhone == null || rawPhone.isEmpty()) {
      return "";
    }
    String formatted = Strings.formatPhone(rawPhone);
    int mode = getPhoneMaskMode();
    if (mode == PHONE_MASK_OFF) {
      return formatted;
    }
    if (isOtherUser && !isMaskAllPhones()) {
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
      int firstSpace = clean.indexOf(' ');
      if (firstSpace != -1 && firstSpace < clean.length() - 1) {
        String countryPart = clean.substring(0, firstSpace);
        String restDigits = clean.substring(firstSpace + 1).replaceAll("[^0-9]", "");
        StringBuilder sb = new StringBuilder(countryPart).append(" ");
        for (int i = 0; i < restDigits.length(); i++) {
          sb.append("*");
        }
        return sb.toString();
      } else {
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
    }

    return formattedPhone;
  }
}
