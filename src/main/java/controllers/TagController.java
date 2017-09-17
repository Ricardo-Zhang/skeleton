package controllers;

import api.ReceiptResponse;
import dao.TagDao;
import dao.ReceiptDao;
import generated.tables.records.ReceiptsRecord;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/tags")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class TagController {
    final TagDao tags;

    public TagController(TagDao tags) { this.tags = tags; }

    @PUT
    @Path("/{tag}")
    public void AddTag(@PathParam("tag") String tagName, int id) {
        tags.AddTag(tagName,id);
    }

    @GET
    @Path("/{tag}")
    public List<ReceiptResponse> taggedReceipts(@PathParam("tag") String tagName) {
        List<ReceiptsRecord> receiptRecords = tags.taggedReceipts(tagName);
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }

}
