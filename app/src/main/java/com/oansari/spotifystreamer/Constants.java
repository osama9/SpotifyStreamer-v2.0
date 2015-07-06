package com.oansari.spotifystreamer;

/**
 * Created by Osama on 7/3/2015.
 */
public class Constants {
    public interface ACTION{
        public static String MAIN_ACTION = "com.oansari.spotifystreamer.main";
        public static String PREV_ACTION = "com.oansari.spotifystreamer.prev";
        public static String PLAY_ACTION = "com.oansari.spotifystreamer.play";
        public static String NEXT_ACTION = "com.oansari.spotifystreamer.next";
        public static String STARTFOREGROUND_ACTION = "com.oansari.spotifystreamer.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.oansari.spotifystreamer.action.stopforeground";
        public static String PREPARED_ACTION = "com.oansari.spotifystreamer.prepared";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}
