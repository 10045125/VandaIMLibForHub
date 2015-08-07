package com.wzl.wzl_vanda.vandaimlibforhub.model;

import com.avos.avoscloud.AVObject;

/**
 * Created by lzw on 15/4/26.
 */
public class UserInfo extends AVObject{

  private String username;
  private String avatarUrl;
  private String avatar;


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatar(String avatarUrl) {
    this.avatar = avatarUrl;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatar = avatarUrl;
  }

}
