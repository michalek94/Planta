package pl.planta.helper;

/**
 * Copyright (C) 2011-2013, Karste Priegnitz
 * <p/>
 * Permission to use, copy, modify, and distribute this piece of software
 * for any purpose with or without fee is hereby granted, provided that
 * the above copyright notice and this permission notice appear in the
 * source code of all copies.
 * <p/>
 * It would be appreciated if you mention the author in your change log,
 * contributors list or the like.
 *
 * @author: Karsten Priegnitz
 * @see: http://code.google.com/p/android-change-log/
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import pl.planta.R;

public class ChangeLog {

    // this is the key for storing the version name in SharedPreferences
    private static final String PREF_NAME = "android_planta";
    private static final String VERSION_KEY = "PREFS_VERSION_KEY";
    private static final String NO_VERSION = "";
    private static final String KEY_END_OF_CHANGE_LOG = "END_OF_CHANGE_LOG";
    private static String TAG = ChangeLog.class.getSimpleName();
    private static Context mContext;
    /**
     * Tryb SharedPreferences |
     * 0 = prywatny, tylko ta aplikacja ma do nich dostęp
     */
    int PRIVATE_MODE = 0;
    private Editor editor;
    private SharedPreferences sharedPreferences;
    private String lastVersion, thisVersion;
    private Listmode listmode = Listmode.NONE;
    private StringBuffer stringBuffer = null;

    public ChangeLog(Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();

        lastVersion = sharedPreferences.getString(VERSION_KEY, NO_VERSION);
        Log.d(TAG, "The latest version: " + lastVersion);
        try {
            thisVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            thisVersion = NO_VERSION;
            Log.e(TAG, "Couldn't get the app version from AndroidManifest.xml file.");
            e.printStackTrace();
        }
        Log.d(TAG, "The app version: " + thisVersion);
    }

    /**
     * @return <code>true</code> if this version of your app is started the first time
     */
    public boolean firstRun() {
        return !lastVersion.equals(thisVersion);
    }

    /**
     * @return <code>true</code> if your app including ChangeLog is started the first time ever.
     * Also <code>true</code> if your app was deinstalled and installed again.
     */
    private boolean firstRunEver() {
        return NO_VERSION.equals(lastVersion);
    }

    /**
     * @return An AlertDialog displaying the changes since the previous installed version of your
     * app (what's new). But when this is the first run of your app including ChangeLog then
     * the full log dialog is show.
     */
    public AlertDialog getLogDialog() {
        return getDialog(firstRunEver());
    }

    /**
     * @return an AlertDialog with a full change log displayed
     */
    private AlertDialog getFullLogDialog() {
        return getDialog(true);
    }

    private AlertDialog getDialog(boolean full) {
        WebView webView = new WebView(mContext);
        webView.loadDataWithBaseURL(null, getLog(full), "text/html", "UTF-8", null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(
                mContext.getResources().getString(full ? R.string.changelog_full_title : R.string.changelog_title))
                .setView(webView)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateVersionInPreferences();
                    }
                });

        if (!full) {
            // Button "More..."
            builder.setNegativeButton("More...", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    getFullLogDialog().show();
                }
            });
        }

        return builder.create();
    }

    private void updateVersionInPreferences() {
        // Zapisz nową wersję aplikacji w SharedPreferences
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor.putString(VERSION_KEY, thisVersion);
        editor.commit();
    }

    /**
     * @return HTML displaying the changes since the previous installed version of your app (what's
     * new)
     */
    public String getLog() {
        return getLog(false);
    }

    /**
     * @return HTML which displays full change log
     */
    public String getFullLog() {
        return getLog(true);
    }

    private String getLog(boolean full) {
        // Czytaj changelog.txt
        stringBuffer = new StringBuffer();
        try {
            InputStream ins = mContext.getResources().openRawResource(R.raw.changelog);
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            String line;
            boolean advanceToEOVS = false; // if true: ignore further version
            // sections
            while ((line = br.readLine()) != null) {
                line = line.trim();
                char marker = line.length() > 0 ? line.charAt(0) : 0;
                if (marker == '$') {
                    // begin of a version section
                    closeList();
                    String version = line.substring(1).trim();
                    // stop output?
                    if (!full) {
                        if (lastVersion.equals(version)) {
                            advanceToEOVS = true;
                        } else if (version.equals(KEY_END_OF_CHANGE_LOG)) {
                            advanceToEOVS = false;
                        }
                    }
                } else if (!advanceToEOVS) {
                    switch (marker) {
                        case '%':
                            // line contains version title
                            closeList();
                            stringBuffer.append("<div class='title'>").append(line.substring(1).trim()).append("</div>\n");
                            break;
                        case '_':
                            // line contains version title
                            closeList();
                            stringBuffer.append("<div class='subtitle'>").append(line.substring(1).trim()).append("</div>\n");
                            break;
                        case '!':
                            // line contains free text
                            closeList();
                            stringBuffer.append("<div class='freetext'>").append(line.substring(1).trim()).append("</div>\n");
                            break;
                        case '#':
                            // line contains numbered list item
                            openList(Listmode.ORDERED);
                            stringBuffer.append("<li>").append(line.substring(1).trim()).append("</li>\n");
                            break;
                        case '*':
                            // line contains bullet list item
                            openList(Listmode.UNORDERED);
                            stringBuffer.append("<li>").append(line.substring(1).trim()).append("</li>\n");
                            break;
                        default:
                            // no special character: just use line as is
                            closeList();
                            stringBuffer.append(line).append("\n");
                    }
                }
            }
            closeList();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }

    private void openList(Listmode listMode) {
        if (listmode != listMode) {
            closeList();
            if (listMode == Listmode.ORDERED) {
                stringBuffer.append("<div class='list'><ol>\n");
            } else if (listMode == Listmode.UNORDERED) {
                stringBuffer.append("<div class='list'><ul>\n");
            }
            listmode = listMode;
        }
    }

    private void closeList() {
        if (listmode == Listmode.ORDERED) {
            stringBuffer.append("</ol></div>\n");
        } else if (listmode == Listmode.UNORDERED) {
            stringBuffer.append("</ul></div>\n");
        }
        listmode = Listmode.NONE;
    }

    /**
     * modes for HTML-Lists (bullet, numbered)
     */
    private enum Listmode {
        NONE, ORDERED, UNORDERED
    }
}
