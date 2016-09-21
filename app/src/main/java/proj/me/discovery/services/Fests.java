package proj.me.discovery.services;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 23/12/15.
 */
public class Fests {

    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("fests")
    @Expose
    private List<Fest> fests = new ArrayList<Fest>();
    @SerializedName("_id")
    @Expose
    private String Id;

    /**
     * @return The count
     */
    public String getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(String count) {
        this.count = count;
    }

    /**
     * @return The fests
     */
    public List<Fest> getFests() {
        return fests;
    }

    /**
     * @param fests The fests
     */
    public void setFests(List<Fest> fests) {
        this.fests = fests;
    }

    /**
     * @return The Id
     */
    public String getId() {
        return Id;
    }

    /**
     * @param Id The _id
     */
    public void setId(String Id) {
        this.Id = Id;
    }
}
