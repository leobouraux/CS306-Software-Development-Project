package ch.epfl.sweng.studyup.utils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public abstract class DataContainers {

    /*
    JSON container objects used for authentication.
    See AuthenticationActivity.
    */

    public static final class TokenContainer {
        @SerializedName("error")
        public String error;

        @SerializedName("access_token")
        public String token;
    }

    public static final class PlayerDataContainer implements Serializable {

        @SerializedName("Firstname")
        public String firstName;

        @SerializedName("Name")
        public String lastname;

        @SerializedName("Sciper")
        public String sciperNum;
    }
}
