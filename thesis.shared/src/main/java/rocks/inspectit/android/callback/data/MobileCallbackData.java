package rocks.inspectit.android.callback.data;

import java.util.ArrayList;
import java.util.List;

import rocks.fasterxml.jackson.annotation.JsonProperty;
import rocks.inspectit.android.Tag;

/**
 * Created by David on 26.10.16.
 */

public class MobileCallbackData {

    @JsonProperty
    private List<MobileDefaultData> childData;

    @JsonProperty
    private List<Tag> tagList;

    @JsonProperty
    private String sessionId;

    @JsonProperty
    private long creationTimestamp;

    public MobileCallbackData() {
        this.creationTimestamp = System.currentTimeMillis();
        this.childData = new ArrayList<MobileDefaultData>();
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public List<MobileDefaultData> getChildData() {
        return childData;
    }

    public void setChildData(List<MobileDefaultData> childData) {
        this.childData = childData;
    }

    public void addChildData(MobileDefaultData data) {
        this.childData.add(data);
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public void clear() {
        creationTimestamp = System.currentTimeMillis();
        this.childData.clear();
        this.tagList.clear();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
