package iShamrock.Postal.database;

import iShamrock.Postal.entity.User;
import iShamrock.Postal.entity.PostalDataItem;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 2/16/15.
 */

/**
 * Implement all the methods
 * View the "User" and "PostalDataItem" class
 */
public class Connect {

    /**
     * @param user
     * @return the user's data, null if login failed
     */
    public User login(String user, String password){
        //todo
        return null;
    }

    /**
     * @param user
     * @return all the postal of the user IN ORDER OF TIME
     */
    public ArrayList<PostalDataItem> getPostalData(String user){
        //todo
        return null;
    }

    /**
     * @param user
     * @return all the friends of the user
     */
    public ArrayList<User> getFriendData(String user){
        //todo
        return null;
    }

    /**
     * Add item to the server
     * @param postalDataItem
     */
    public void addPostal(PostalDataItem postalDataItem){
        //todo
    }

    /**
     * Delete the item from the server
     * @param postalDataItem
     */
    public void deletePostal(PostalDataItem postalDataItem){
        //todo
    }

    public void addFriend(String user, User friend){
        //todo
    }

    public void deleteFriend(String user, User friend){
        //todo
    }
}
