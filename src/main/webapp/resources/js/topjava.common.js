let form;

function makeEditable(datatableApi) {
    ctx.datatableApi = datatableApi;
    form = $('#detailsForm');
    $(".delete").click(function () {
        if (confirm('Are you sure?')) {
            deleteRow($(this).closest('tr').attr("id"));
        }
    });

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});
}

function add() {
    form.find(":input").val("");
    $("#editRow").modal();
}

function deleteRow(id) {
    $.ajax({
        url: ctx.ajaxUrl + id,
        type: "DELETE"
    }).done(function () {
        ctx.updateTable();
        successNoty("Deleted");
    });
}

function updateDataTable(data) {
    ctx.datatableApi.clear().rows.add(data).draw();
}

const defaultUpdateTable = function updateTable() {
    $.get(ctx.ajaxUrl, function (data) {
        updateDataTable(data)
    });
}

function save() {
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        ctx.updateTable();
        successNoty("Saved");
    });
}

let failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(text) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + text,
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function failNoty(jqXHROrText) {
    closeNoty();

    let errorText;
    if (typeof jqXHROrText === 'string') {
        errorText = jqXHROrText;
    } else if (jqXHROrText.status === 0) {
        errorText = "Server unavailable. Check connection.";
    } else if (jqXHROrText.status >= 400 && jqXHROrText.status < 500) {
        errorText = "Client error: " + jqXHROrText.status;
    } else if (jqXHROrText.status >= 500) {
        errorText = "Server error: " + jqXHROrText.status;
    } else {
        errorText = "Error:  " + jqXHROrText.status;
    }

    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;" + errorText,
        type: "error",
        layout: "bottomRight"
    });
    failedNote.show();
}