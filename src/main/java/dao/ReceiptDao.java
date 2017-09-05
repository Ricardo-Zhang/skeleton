package dao;

import api.ReceiptResponse;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAG;


public class ReceiptDao {
    DSLContext dsl;

    public ReceiptDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String merchantName, BigDecimal amount) {
        ReceiptsRecord receiptsRecord = dsl
                .insertInto(RECEIPTS, RECEIPTS.MERCHANT, RECEIPTS.AMOUNT)
                .values(merchantName, amount)
                .returning(RECEIPTS.ID)
                .fetchOne();

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        return receiptsRecord.getId();
    }

    public void tag(String tagName, int id){
        boolean Exist = dsl.fetchExists(
                dsl.selectOne()
                    .from(TAG)
                    .where(TAG.ID.eq(id),
                            TAG.TAGNAME.eq(tagName))
        );
        if (Exist){
            dsl.delete(TAG)
                    .where(TAG.ID.eq(id),
                            TAG.TAGNAME.eq(tagName))
                    .execute();
        }
        else{
            dsl.insertInto(TAG,TAG.TAGNAME,TAG.ID)
                    .values(tagName, id)
                    .execute();
        }
    }

    public List<ReceiptsRecord> getTags(String tagName){
        return dsl.selectFrom(RECEIPTS)
                .where(RECEIPTS.ID.in(
                        dsl.select(TAG.ID)
                        .from(TAG)
                        .where(TAG.TAGNAME.eq(tagName))
                        .fetch()
                        .getValues("id")
                ))
                .fetch();
    }
    public List<ReceiptsRecord> getAllReceipts() {
        return dsl.selectFrom(RECEIPTS).fetch();
    }
}
