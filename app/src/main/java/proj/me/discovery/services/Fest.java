package proj.me.discovery.services;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fest {

@SerializedName("fest_name")
@Expose
private String festName;
@SerializedName("fest_place")
@Expose
private String festPlace;
@SerializedName("fest_image")
@Expose
private String festImage;
@SerializedName("fest_like_count")
@Expose
private String festLikeCount;
@SerializedName("fest_isLiked")
@Expose
private String festIsLiked;

/**
* 
* @return
* The festName
*/
public String getFestName() {
return festName;
}

/**
* 
* @param festName
* The fest_name
*/
public void setFestName(String festName) {
this.festName = festName;
}

/**
* 
* @return
* The festPlace
*/
public String getFestPlace() {
return festPlace;
}

/**
* 
* @param festPlace
* The fest_place
*/
public void setFestPlace(String festPlace) {
this.festPlace = festPlace;
}

/**
* 
* @return
* The festImage
*/
public String getFestImage() {
return festImage;
}

/**
* 
* @param festImage
* The fest_image
*/
public void setFestImage(String festImage) {
this.festImage = festImage;
}

/**
* 
* @return
* The festLikeCount
*/
public String getFestLikeCount() {
return festLikeCount;
}

/**
* 
* @param festLikeCount
* The fest_like_count
*/
public void setFestLikeCount(String festLikeCount) {
this.festLikeCount = festLikeCount;
}

/**
* 
* @return
* The festIsLiked
*/
public String getFestIsLiked() {
return festIsLiked;
}

/**
* 
* @param festIsLiked
* The fest_isLiked
*/
public void setFestIsLiked(String festIsLiked) {
this.festIsLiked = festIsLiked;
}

}