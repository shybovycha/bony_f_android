package tk.shybovycha.bony_f.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import com.facebook.*;
import com.facebook.login.LoginResult;
import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shybovycha on 27/06/15.
 */
public class FacebookLoginCallback implements FacebookCallback<LoginResult> {
    private String email;
    private String name;
    private char gender;
    private String id;
    private String profilePicture;

    @Override
    public void onSuccess(LoginResult loginResults) {
        fetchProfileData(loginResults);
    }

    @Override
    public void onCancel() {
        Log.d("MOOFB:RESPONSE", "FB login cancelled");
    }

    @Override
    public void onError(FacebookException e) {
        Log.d("MOOFB:RESPONSE", "FB login failed: " + e.getMessage());
    }

    protected void fetchProfileData(LoginResult loginResults) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResults.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        parseProfileData(object);

                        Log.d("MOOFB:RESPONSE", response.toString());
                        Log.d("MOOFB:OBJECT", object.toString());
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    protected void parseProfileData(JSONObject object) {
        try {
            this.id = object.getString("id");
            this.email = object.getString("email");
            this.name = object.getString("name");
            this.gender = object.getString("gender").equals("male") ? 'm' : 'f';
            fetchProfilePicture();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void fetchProfilePicture() {
//        new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                String.format("/{0}/picture", this.id),
//                null,
//                HttpMethod.GET,
//                new GraphRequest.Callback() {
//                    public void onCompleted(GraphResponse response) {
//                        decodePicture(response);
//                    }
//                }
//        ).executeAsync();
        this.profilePicture = String.format("https://graph.facebook.com/%s/picture?type=large", this.id);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public char getGender() {
        return gender;
    }

    public String getId() {
        return id;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}
