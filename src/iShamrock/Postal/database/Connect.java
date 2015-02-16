package iShamrock.Postal.database;

import iShamrock.Postal.entity.Friend;
import iShamrock.Postal.entity.PostalDataItem;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 2/16/15.
 */

/**
 * Implement all the methods
 * View the "Friend" and "PostalDataItem" class
 */
public class Connect {

    /**
     * @param user
     * @return the user's data
     * (The Friend class seems to fulfill the need)
     */
    private Friend getUserData(String user){
        //todo
        return null;
    }

    /**
     * @param user
     * @return all the postal of the user IN ORDER OF TIME
     */
    private ArrayList<PostalDataItem> getPostalData(String user){
        //todo
        return null;
    }

    /**
     * @param user
     * @return all the friends of the user
     */
    private ArrayList<Friend> getFriendData(String user){
        //todo
        return null;
    }

    /**
     * Add item to the server
     * @param postalDataItem
     */
    private void addPostal(PostalDataItem postalDataItem){
        //todo
    }

    /**
     * Delete the item from the server
     * @param postalDataItem
     */
    private void deletePostal(PostalDataItem postalDataItem){
        //todo
    }

    private void addFriend(String user, Friend friend){
        //todo
    }

    private void deleteFriend(String user, Friend friend){
        //todo
    }
}
