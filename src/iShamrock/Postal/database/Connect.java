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
     * @param phone
     * @return the user's data, null if login failed
     */
    public User login(String phone, String password){
        //todo: remember to download the profile photo if it doesn't exist
        return null;
    }

    /**
     * @param user
     * @return true if sign up succeed
     */
    public boolean signUp(User user){
        //todo:
        return true;
    }

    /**
     * @param user
     */
    public ArrayList<PostalDataItem> getPostalData(User user){
        //todo: changed the parameter from String to User
        //todo: return all the postal if the to_user or the from_user is parameter user
        //todo: don't forget download matters
        return null;
    }

    /**
     * Add item to the server
     * @param postalDataItem
     */
    public void addPostal(PostalDataItem postalDataItem){
        //todo: remember to store the Uri contents on the server
    }

    /**
     * @param user
     * @return all the friends of the user
     */
    public ArrayList<User> getFriendData(User user){
        //todo: return all the friends of the user
        return null;
    }

    /**
     * @param user
     * @param friend
     */
    public void addFriend(User user, User friend){
        //todo: add a friend
    }

    public void getAllUser(){
        //todo: return all the users, it's used when the user wants to add a friend
    }


    /*The following methods are not important*/

    /**
     * Delete the item from the server
     * @param postalDataItem
     */
    public void deletePostal(PostalDataItem postalDataItem){
        //todo
    }


    public void deleteFriend(String user, User friend){
        //todo
    }

    public void updateUserInformation(User user, User updatedUser){

    }



}
