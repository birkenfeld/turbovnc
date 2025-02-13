/* Copyright (C) 2012 Brian P. Hinz
 * Copyright (C) 2012, 2015, 2018, 2020 D. R. Commander.  All Rights Reserved.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */

package com.turbovnc.rfb;

import javax.swing.filechooser.FileSystemView;

public final class Utils {

  public static final int JAVA_VERSION =
    Integer.parseInt(System.getProperty("java.version").split("\\.")[0]) <= 1 ?
    Integer.parseInt(System.getProperty("java.version").split("\\.")[1]) :
    Integer.parseInt(System.getProperty("java.version").split("\\.")[0]);

  private static final String OS = System.getProperty("os.name").toLowerCase();

  public static boolean isMac() {
    return OS.startsWith("mac os x");
  }

  public static boolean isWindows() {
    return OS.startsWith("windows");
  }

  public static boolean isX11() {
    return !isMac() && !isWindows();
  }

  public static boolean osEID() {
    return !isWindows();
  }

  public static boolean osGrab() {
    return !isMac();
  }

  public static boolean getBooleanProperty(String key, boolean def) {
    String prop = System.getProperty(key, def ? "True" : "False");
    if (prop != null && prop.length() > 0) {
      if (prop.equalsIgnoreCase("true") || prop.equalsIgnoreCase("yes"))
        return true;
      if (prop.equalsIgnoreCase("false") || prop.equalsIgnoreCase("no"))
        return false;
      int i = -1;
      try {
        i = Integer.parseInt(prop);
      } catch (NumberFormatException e) {}
      if (i == 1)
        return true;
      if (i == 0)
        return false;
    }
    return def;
  }

  public static String getFileSeparator() {
    String separator = null;
    try {
      separator = Character.toString(java.io.File.separatorChar);
    } catch (java.security.AccessControlException e) {
      vlog.error("Cannot access file.separator system property:");
      vlog.error("  " + e.getMessage());
    }
    return separator;
  }

  public static String getHomeDir() {
    String homeDir = null;
    try {
      if (isWindows()) {
        // JRE prior to 1.5 cannot reliably determine USERPROFILE.
        // Return user.home and hope it's right.
        if (JAVA_VERSION < 5) {
          try {
            homeDir = System.getProperty("user.home");
          } catch (java.security.AccessControlException e) {
            vlog.error("Cannot access user.home system property:");
            vlog.error("  " + e.getMessage());
          }
        } else {
          homeDir = System.getenv("USERPROFILE");
        }
      } else {
        try {
          homeDir = FileSystemView.getFileSystemView().
                    getDefaultDirectory().getCanonicalPath();
        } catch (java.security.AccessControlException e) {
          vlog.error("Cannot access system property:");
          vlog.error("  " + e.getMessage());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return homeDir + getFileSeparator();
  }

  public static String getVncHomeDir() {
    return getHomeDir() + ".vnc" + getFileSeparator();
  }

  public static double getTime() {
    return (double)System.nanoTime() / 1.0e9;
  }

  private Utils() {}
  static LogWriter vlog = new LogWriter("Utils");
}
