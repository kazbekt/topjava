const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    updateTable: defaultUpdateTable
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

function enableUser(userId, enabled, userRow) {
    const originalEnabled = !enabled;

    $.ajax({
        url: ctx.ajaxUrl + userId + '/enable?enabled=' + enabled,
        type: 'PATCH',
        success: function () {
            if (enabled) {
                userRow.removeClass('text-muted');
            } else {
                userRow.addClass('text-muted');
            }
            successNoty(enabled ? 'User enabled' : 'User disabled');
        },
        error: function (xhr) {
            const checkbox = userRow.find('.user-enabled-checkbox');
            checkbox.prop('checked', originalEnabled);

            if (originalEnabled) {
                userRow.removeClass('text-muted');
            } else {
                userRow.addClass('text-muted');
            }

            failNoty(xhr);
        }
    });
}

$(function () {
    $(document).on('change', '.user-enabled-checkbox', function () {
        const $checkbox = $(this);
        enableUser(
            $checkbox.data('user-id'),
            $checkbox.is(':checked'),
            $checkbox.closest('tr')
        );
    });
});

