package dao;

import api.ReceiptResponse;
import generated.tables.records.ReceiptsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;

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

    public int tag(String tagName, int id){
        boolean Exist = dsl.fetchExists(
                dsl.selectOne()
                    .from(DSL.table("tag"))
                    .where(DSL.field("id").eq(id),
                            DSL.field("tagname").eq(tagName))
        );
        if (Exist){
            dsl.delete(DSL.table("tag"))
                    .where(DSL.field("id").eq(id),
                            DSL.field("tagname").eq(tagName))
                    .execute();
            return 1;
        }
        else{
            dsl.insertInto(DSL.table("tag"),DSL.field("tagname"),DSL.field("id"))
                    .values(tagName, id)
                    .execute();
            return 2;
        }
    }

    public List<ReceiptsRecord> getAllTags(){
        return dsl.selectFrom(RECEIPTS)
                .where(RECEIPTS.ID.in(
                        dsl.select(DSL.field("id"))
                        .from(DSL.table("tag"))
                        .fetch()
                        .getValues("id")
                ))
                .fetch();
    }
    public List<ReceiptsRecord> getAllReceipts() {
        return dsl.selectFrom(RECEIPTS).fetch();
    }
}
