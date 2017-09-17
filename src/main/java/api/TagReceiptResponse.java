package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.List;
import java.util.ArrayList;

/**
 * This is an API Object.  Its purpose is to model the JSON API that we expose.
 * This class is NOT used for storing in the Database.
 *
 * This ReceiptResponse in particular is the model of a Receipt that we expose to users of our API
 *
 * Any properties that you want exposed when this class is translated to JSON must be
 * annotated with {@link JsonProperty}
 */
public class TagReceiptResponse {
    @JsonProperty
    Integer id;

    @JsonProperty
    String merchantName;

    @JsonProperty
    BigDecimal value;

    @JsonProperty
    Time created;

    @JsonProperty
    String [] tagnames;
    public TagReceiptResponse(ReceiptsRecord dbRecord) {
        this.merchantName = dbRecord.getMerchant();
        this.value = dbRecord.getAmount();
        this.created = dbRecord.getUploaded();
        this.id = dbRecord.getId();
    }
    public void tagArray(List<TagsRecord> tags){
        ArrayList<String> taglist = new ArrayList<>();
        for (TagsRecord tag : tags) {
            taglist.add(tag.getTagname());
        }
    this.tagnames = taglist.toArray(new String[0]);
  }
}
