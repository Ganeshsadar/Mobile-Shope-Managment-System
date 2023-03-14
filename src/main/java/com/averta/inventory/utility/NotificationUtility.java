package com.averta.inventory.utility;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.averta.inventory.entity.Notifications;
import com.averta.inventory.entity.User;
import com.averta.inventory.repository.ObjectDao;


@Component
public class NotificationUtility {

    @Autowired
    private Environment environment;

    @Autowired
    private ObjectDao objectDao;

    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    @SuppressWarnings("unchecked")
    public void sendNotification(String header, String body, String deviceId, User user) throws Exception {
        try {
            Notifications noti = new Notifications();
            noti.setDeviceId(deviceId);
            noti.setNotificationBody(body);
            noti.setNotificationHeader(header);
            noti.setUser(user);
            noti.setStatus("Sent");
            noti.setIsSeen(false);
            objectDao.saveObject(noti);

            if (deviceId != null && !deviceId.isEmpty()) {
                String authKey = environment.getRequiredProperty("fcm.server.key"); // You FCM AUTH key
                String FMCurl = API_URL_FCM;
                URL url = new URL(FMCurl);
                HttpURLConnection conn = null;
                JSONObject info = new JSONObject();
                info.put("body", body);
                info.put("title", header);
                info.put("sound", "default");
                info.put("click_action", "FCM_PLUGIN_ACTIVITY");
                JSONObject data = new JSONObject();

                data.put("param2", header);

                JSONArray mJSONArray = new JSONArray();
                mJSONArray.add(deviceId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "key=" + authKey);
                conn.setRequestProperty("Content-Type", "application/json");
                JSONObject json = new JSONObject();
                json.put("notification", info);
                json.put("data", data);
                json.put("registration_ids", mJSONArray);
                json.put("priority", "high");
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();
                System.out.println("status code : " + conn.getResponseCode());
                System.out.println("status message : " + conn.getResponseMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}