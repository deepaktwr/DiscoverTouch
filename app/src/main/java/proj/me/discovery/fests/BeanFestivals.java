package proj.me.discovery.fests;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 18/12/15.
 */
public class BeanFestivals implements Parcelable{
    public BeanFestivals(){
    }

    private String festImage, festName, festPlace;
    private int festId;

    private int bodyTextColor, titleTextColor, mutedColor, likeCount;

    private String imageLink;

    private boolean doesLike;

    protected BeanFestivals(Parcel in) {
        festImage = in.readString();
        festName = in.readString();
        festPlace = in.readString();
        festId = in.readInt();
        bodyTextColor = in.readInt();
        titleTextColor = in.readInt();
        mutedColor = in.readInt();
        imageLink = in.readString();
        likeCount = in.readInt();
        doesLike = in.readByte()!=0;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isDoesLike() {
        return doesLike;
    }

    public void setDoesLike(boolean doesLike) {
        this.doesLike = doesLike;
    }


    public int getBodyTextColor() {
        return bodyTextColor;
    }

    public void setBodyTextColor(int bodyTextColor) {
        this.bodyTextColor = bodyTextColor;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public void setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    public int getMutedColor() {
        return mutedColor;
    }

    public void setMutedColor(int mutedColor) {
        this.mutedColor = mutedColor;
    }

    public static final Creator<BeanFestivals> CREATOR = new Creator<BeanFestivals>() {
        @Override
        public BeanFestivals createFromParcel(Parcel in) {
            return new BeanFestivals(in);
        }

        @Override
        public BeanFestivals[] newArray(int size) {
            return new BeanFestivals[size];
        }
    };

    public int getFestId() {
        return festId;
    }

    public void setFestId(int festId) {
        this.festId = festId;
    }

    public String getFestImage() {
        return festImage;
    }

    public void setFestImage(String festImage) {
        this.festImage = festImage;
    }

    public String getFestName() {
        return festName;
    }

    public void setFestName(String festName) {
        this.festName = festName;
    }

    public String getFestPlace() {
        return festPlace;
    }

    public void setFestPlace(String festPlace) {
        this.festPlace = festPlace;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(festImage);
        dest.writeString(festName);
        dest.writeString(festPlace);
        dest.writeInt(festId);
        dest.writeInt(bodyTextColor);
        dest.writeInt(titleTextColor);
        dest.writeInt(mutedColor);
        dest.writeString(imageLink);
        dest.writeInt(likeCount);
        dest.writeByte((byte)(doesLike ? 1:0));
    }
}
