const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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
                    "asc"
                ]
            ]
        })
    );
});

function enableUser(userId, enabled) {
    const userRow = $('tr[id="' + userId + '"]');

    $.ajax({
        url: ctx.ajaxUrl + userId + '/enable',
        type: 'POST',
        data: { enabled: enabled },
        success: function () {
            if (enabled) {
                userRow.removeClass('text-muted');
            } else {
                userRow.addClass('text-muted');
            }
            successNoty(enabled ? 'User enabled' : 'User disabled');
        }
    });
}

$(function () {
    $(document).on('change', '.user-enabled-checkbox', function () {
        const userId = $(this).data('user-id');
        const enabled = $(this).is(':checked');
        enableUser(userId, enabled);
    });
});

