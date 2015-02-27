package iShamrock.Postal.database;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import iShamrock.Postal.entity.PostalData;
import iShamrock.Postal.entity.PostalDataItem;
import iShamrock.Postal.entity.User;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lifengshuang on 2/16/15.
 */

/**
 * Implement all the methods
 * View the "User" and "PostalDataItem" class
 */
public class Connect {
    public final String server="http://121.40.155.146:8080/postalservermine/data/";



    public String logintests(String user, String password) {
       // String server="http://121.40.155.146:8080/postalservermine/data/";
        String url = server + "UserLogin?phone=" + user + "&password=" + password;

        System.out.println(url);
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream is = null;


                is = response.getEntity().getContent();


                String result = inStream2String(is);
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * @param user
     * @return the user's data, null if login failed
     */
    public User login(String user, String password) throws IOException {

        String url = server + "UserLogin";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("phone", user));
        urlParameters.add(new BasicNameValuePair("password", password));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        String name=rd.readLine();
        String phone=rd.readLine();
        String photoURI=rd.readLine();
        String coverURI=rd.readLine();
        name=(name.equals("null"))?null:name;
        phone=(phone.equals("null"))?null:phone;
        photoURI=(photoURI.equals("null"))?null:photoURI;
        coverURI=(coverURI.equals("null"))?null:coverURI;

        User u=new User(name,phone,photoURI,coverURI);


        return u;
    }
    private String inStream2String(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        return new String(baos.toByteArray());
    }
    public ArrayList<String> logintest(String user, String password){
        String strurl=server+"UserLogin";
        URL url=null;
        try {
            url= new URL(strurl);
            HttpURLConnection urlConn= (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setRequestMethod("post");
            urlConn.setUseCaches(false);
            // set requestproperty;
            urlConn.setRequestProperty("Charset","utf-8");
            urlConn.connect();

            DataOutputStream dop=new DataOutputStream(urlConn.getOutputStream());
            dop.writeBytes("param="+ URLEncoder.encode("","utf-8"));
            dop.flush();
            dop.close();

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            ArrayList<String> result=new ArrayList<String>();
            String readLine=null;
            while((readLine=bufferedReader.readLine())!=null){
                result.add(readLine);

            }
            bufferedReader.close();
            urlConn.disconnect();

            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //todo

        return null;
    }
    /**
     * @param user
     * @return all the postal of the user IN ORDER OF TIME
     */


    public ArrayList<PostalDataItem> getPostalData(User user) throws IOException {

        //todo: changed the parameter from String to User
        //todo: return all the postal if the to_user or the from_user is parameter user
        //todo: don't forget download matters
        String url = server + "GetPostalItem";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("phone", user.getPhone()));


        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        ArrayList<PostalDataItem> apid = new ArrayList<PostalDataItem>();
        int n = Integer.valueOf(rd.readLine());
        for (int i = 0; i < n; i++) {
            int type = Integer.valueOf(rd.readLine());
            String uri = rd.readLine();
            String text = rd.readLine();
            String time = rd.readLine();
            String title = rd.readLine();
            double locationx = Double.valueOf(rd.readLine());
            double locationy = Double.valueOf(rd.readLine());
            String from_user = rd.readLine();
            String to_user = rd.readLine();
            String location_text = rd.readLine();


            uri = (uri.equals("null")) ? null : uri;
            text = (text.equals("null")) ? null : text;
            time = (time.equals("null")) ? null : time;
            title = (title.equals("null")) ? null : title;
            from_user = (from_user.equals("null")) ? null : from_user;
            to_user = (to_user.equals("null")) ? null : to_user;
            location_text = (location_text.equals("null")) ? null : location_text;
            double[] location = {locationx, locationy};

            PostalDataItem pdi = new PostalDataItem(type, uri, text, time, title, location, from_user, to_user, location_text);
            apid.add(pdi);
        }

        return apid;
    }




    public void addPostal(PostalDataItem postalDataItem) throws IOException {
        //todo
        String url = server + "AddPostalItem";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("type", postalDataItem.type+""));
        urlParameters.add(new BasicNameValuePair("uri", postalDataItem.uri));
        urlParameters.add(new BasicNameValuePair("text", postalDataItem.text));
        urlParameters.add(new BasicNameValuePair("time", postalDataItem.time));
        urlParameters.add(new BasicNameValuePair("title", postalDataItem.title));
        urlParameters.add(new BasicNameValuePair("locationx", postalDataItem.location[0]+""));
        urlParameters.add(new BasicNameValuePair("locationy", postalDataItem.location[1]+""));
        urlParameters.add(new BasicNameValuePair("from", postalDataItem.from_user));
        urlParameters.add(new BasicNameValuePair("to", postalDataItem.to_user));
        urlParameters.add(new BasicNameValuePair("locationtext", postalDataItem.location_text));


        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));


        rd.close();

    }


    public void addFriend(User user, User friend) throws IOException {


        String url = server + "AddFriend";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("user", user.getPhone()));
        urlParameters.add(new BasicNameValuePair("friend", friend.getPhone()));


        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));


        rd.close();

    }


    public void signUp(User user,String password) throws IOException {
        //todo:
        String url = server + "UserRegister";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("phone", user.getPhone()));
        urlParameters.add(new BasicNameValuePair("password", password));


        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));


        rd.close();

        if(user.getNickname()!=null){
            updateUserInformation(user,user);
        }

    }



    /**
     * @param user
     * @return all the friends of the user
     */
    public ArrayList<User> getFriendData(User user){
        //todo: return all the friends of the user
        String url = server + "GetAllFriendsData";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("phone", user.getPhone()));
        ArrayList<User> auser = new ArrayList<User>();

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response = null;
        try {
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            auser = new ArrayList<User>();
            int n = Integer.valueOf(rd.readLine());
            for (int i = 0; i < n; i++) {

                String name=rd.readLine();
                String phone=rd.readLine();
                String photoURI=rd.readLine();
                String coverURI=rd.readLine();
                name=(name.equals("null"))?null:name;
                phone=(phone.equals("null"))?null:phone;
                photoURI=(photoURI.equals("null"))?null:photoURI;
                coverURI=(coverURI.equals("null"))?null:coverURI;

                User u=new User(name,phone,photoURI,coverURI);
                auser.add(u);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        return auser;

    }



    public ArrayList<User> getAllUser(){
        //todo: return all the users, it's used when the user wants to add a friend
        String url = server + "GetAllUser";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");
        ArrayList<User> auser = new ArrayList<User>();

        HttpResponse response = null;
        try {
            response = client.execute(post);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            auser = new ArrayList<User>();
            int n = Integer.valueOf(rd.readLine());
            for (int i = 0; i < n; i++) {

                String name=rd.readLine();
                String phone=rd.readLine();
                String photoURI=rd.readLine();
                String coverURI=rd.readLine();
                name=(name.equals("null"))?null:name;
                phone=(phone.equals("null"))?null:phone;
                photoURI=(photoURI.equals("null"))?null:photoURI;
                coverURI=(coverURI.equals("null"))?null:coverURI;

                User u=new User(name,phone,photoURI,coverURI);
                auser.add(u);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        return auser;

    }








    /*The following methods are not important*/



    public void updateUserInformation(User user, User updatedUser) throws IOException {
        String url = server + "UpdateUserInformation";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "android");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("phone", user.getPhone()));
        urlParameters.add(new BasicNameValuePair("name2", updatedUser.getNickname()));
        urlParameters.add(new BasicNameValuePair("phone2", updatedUser.getPhone()));
        urlParameters.add(new BasicNameValuePair("photoURI2", updatedUser.getPhotoURI()));
        urlParameters.add(new BasicNameValuePair("coverURI2", updatedUser.getCoverURI()));



        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));


        rd.close();

    }
    public void deleteFriend(User user, User friend){
        //todo
    }
    /**
     * Delete the item from the server
     * @param postalDataItem
     */
    public void deletePostal(PostalDataItem postalDataItem){
        //todo
    }

}


