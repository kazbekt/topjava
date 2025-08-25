const mealAjaxUrl = "profile/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: filter
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

function filter() {
    var formData = $("#filterForm").serialize();
    $.get(ctx.ajaxUrl + "filter?" + formData, function (data) {
        updateDataTable(data);
    });
    return false;
}

function resetFilter() {
    $("#filterForm")[0].reset();
    ctx.updateTable();
}