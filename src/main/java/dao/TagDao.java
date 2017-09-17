package dao;

import generated.tables.records.TagsRecord;
import generated.tables.records.ReceiptsRecord;

import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.Result;

import java.util.List;


public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public void AddTag(String tagname, Integer id) {
        boolean tagged =  dsl.fetchExists(dsl.selectFrom(TAGS)
                .where(TAGS.ID.eq(id)).and(TAGS.TAGNAME.eq(tagname)));
        if (tagged) {
            dsl.deleteFrom(TAGS).where(TAGS.ID.eq(id)).and(TAGS.TAGNAME.eq(tagname)).execute();
        } else {
            dsl.insertInto(TAGS, TAGS.ID, TAGS.TAGNAME).values(id, tagname).execute();
        }
    }

    public List<ReceiptsRecord> taggedReceipts(String tagname) {
        return dsl.selectFrom(RECEIPTS).where(RECEIPTS.ID.in(dsl.selectFrom(TAGS).where(TAGS.TAGNAME.eq(tagname)).fetch().getValues(TAGS.ID))).fetch();
    }
}
