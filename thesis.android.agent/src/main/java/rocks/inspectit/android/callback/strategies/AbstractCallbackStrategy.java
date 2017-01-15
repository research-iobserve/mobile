package rocks.inspectit.android.callback.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rocks.fasterxml.jackson.core.JsonProcessingException;
import rocks.fasterxml.jackson.databind.ObjectMapper;
import rocks.inspectit.android.ExternalConfiguration;
import rocks.inspectit.android.Tag;
import rocks.inspectit.android.TagCollector;
import rocks.inspectit.android.callback.CallbackTask;
import rocks.inspectit.android.callback.data.MobileCallbackData;
import rocks.inspectit.android.callback.data.MobileDefaultData;
import rocks.inspectit.android.util.DependencyInjector;

/**
 * Created by David on 25.10.16.
 */

public abstract class AbstractCallbackStrategy {
    private static ObjectMapper mapper = new ObjectMapper();

    protected MobileCallbackData data;
    private TagCollector tagCollector = DependencyInjector.getTagCollector();

    public AbstractCallbackStrategy() {
        this.data = new MobileCallbackData();
    }

    abstract public void addData(MobileDefaultData data);

    abstract public void stop();

    public void sendImmediately(MobileCallbackData data, boolean isHello) {
        this.sendBeacon(data, isHello);
        data.clear();
    }

    public void setSessId(String id) {
        this.data.setSessionId(id);
    }

    protected synchronized void sendBeacon() {
        this.sendBeacon(this.data, false);
        data.clear();
    }

    private void sendBeacon(MobileCallbackData data, boolean helloReq) {
        if (data != null && data.getChildData().size() > 0) {
            if (data.getTagList() == null || data.getTagList().size() == 0) {
                tagCollector.collectDynamicTags();
                Set<Tag> tagSet = tagCollector.getDynamicTags();
                List<Tag> tagList = new ArrayList<Tag>(tagSet);
                data.setTagList(tagList);
            }

            String callbackUrl;
            if (helloReq) {
                callbackUrl = ExternalConfiguration.getHelloUrl();
            } else {
                callbackUrl = ExternalConfiguration.getBeaconUrl();
            }

            try {
                new CallbackTask(callbackUrl).execute(mapper.writeValueAsString(data));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

}
