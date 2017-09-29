    // This is the idiomatic way to ensure that JQuery does not
    // execute until the page has loaded
    $(function(){
        const api = "http://localhost:8080"; // <- do not need a root api URL if this page is served directly by the API
        var allReceipts;
        $(reloadReceipts)
        function reloadReceipts() {
          $(".receipt").remove();

        $.getJSON(api+"/receipts.json", function(receipts){
            for(var i=0; i < receipts.length; i++) {
                var receipt = receipts[i];
                receipt.created = getElapsedTime(receipt.created);

                var tagShow;
                for(var j=0; j<receipt.tags.length;j++) {
                    tag = receipt.tags[j];
                    tagShow = tagShow + '<div class="tagValue" onclick="untag('+i+','+j+')"' + tag +'&ensp;<button class="removeTagButtom">&#10005;</button></div>';
                }
                $(`<tr class="receipt">
                        <td class="time">${receipt.created}</td>
                        <td class="merchant">${receipt.merchantName}</td>
                        <td class="amount">${receipt.value}$</td>
                        ${tagHTML}
                    </tr>`).appendTo($("#receiptsTable"));
            }
        })
    })
  }
    function cancelAdding () {
            hidePanel();
        }
        function saveAdding () {
            var merchantName = $('#merchant').val();
            var amountValue = $('#amount').val();
            if (merchantName == "") {
                setErrorMessage('Please input Merchant name!');
                return;
            }
            if (amountValue != '' && !$.isNumeric(amountValue)) {
                setErrorMessage('Please input numberic Amount value!');
                return;
            }
            var parameters = {"merchant": merchantName, "amount": amountValue};
            AJAX_createReceipt(parameters);
        }
        function setErrorMessage(message) {
            $('#panelErrorView').text(message);
        }
        function clearErrorMessage() {
            $('#panelErrorView').text('');
        }
        function removeTag(recordIndex, tagIndex) {
            var thisRecord = allReceipts[recordIndex]
            var recordId = thisRecord.id;
            var tagName = thisRecord.tags[tagIndex];
            AJAX_updateTag(recordId, tagName);
        }
        function showPanel() {
            // load template for add receipt panel
            var template = $('#addReceip-template').html();
            $('#panelView').append(template);
            $("#add-receipt").attr("disabled", true);
        }
        function hidePanel() {
            $(".addReceiptPanel").remove();
            $("#add-receipt").attr("disabled", false);
        }
        function showTagInput(recordIndex) {
            // load template for tag name input
            var template = $('#tagInput-template').html();
            var thisRecord = allReceipts[recordIndex]
            var thisReceipt = $(".receipt")[recordIndex];
            var thisTagNode = $(".tags", thisReceipt);
            thisTagNode.append(template);
            $(".add-tag", thisReceipt).attr("disabled", true);
            $(".tag_input", thisTagNode).on('keypress', function (e) {
                if(e.which === 13){
                    var tagName = $(this).val();
                    if (tagName == '') return;
                    $(this).attr("disabled", "disabled");
                    AJAX_updateTag(thisRecord.id, tagName, thisTagNode);
                }
            });
        }
        function hideTagInput(tagNode) {
            $(".tag_input", tagNode).remove();
            $(".add-tag", $(".receipt")[recordIndex]).attr("disabled", false);
        }

    <!-- time converter -->

        function getElapsedTime(timestamp) {
            var currentTimestamp = new Date().getTime();
            var elapsed = currentTimestamp - timestamp;
            return msToTime(elapsed);
        }
        function msToTime(duration) {
            var milliseconds = parseInt((duration%1000)/100)
                , seconds = parseInt((duration/1000)%60)
                , minutes = parseInt((duration/(1000*60))%60)
                , hours = parseInt((duration/(1000*60*60))%24);
            hours = (hours == 0) ? "": hours + "h ";
            minutes = (minutes == 0 && hours == 0) ? "": minutes + "m ";
            seconds = seconds + "s ago"
            return hours + minutes + seconds;
        }

    <!-- AJAX -->

        function AJAX_createReceipt(parameters) {
            $.ajax({
                beforeSend: function(xhrObj){
                    xhrObj.setRequestHeader("Content-Type","application/json");
                    xhrObj.setRequestHeader("Accept","application/json");
                },
                url: api+'/receipts',
                type: 'POST',
                data: JSON.stringify(parameters),
                success: function(result) {
                    clearErrorMessage();
                    hidePanel();
                    $(reloadReceipts)
                },
                error: function(jqXHR, exception) {
                    var err = 'Error: ' + jqXHR.responseText;
                    setErrorMessage(err);
                }
            })
        }
        function AJAX_updateTag(recordId, tagName, tagNode) {
            $.ajax({
                beforeSend: function(xhrObj){
                    xhrObj.setRequestHeader("Content-Type","application/json");
                    xhrObj.setRequestHeader("Accept","application/json");
                },
                url: api+'/tags/' + tagName,
                type: 'PUT',
                data: JSON.stringify(recordId),
                success: function(result) {
                    if (tagNode != null) {
                    }
                    $(reloadReceipts)
                },
                error: function(jqXHR, exception) {
                    var err = 'Error: ' + jqXHR.responseText;
                    setErrorMessage(err);
                }
            })
        }
