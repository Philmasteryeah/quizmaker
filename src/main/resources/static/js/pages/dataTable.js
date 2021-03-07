$(function() {

	'use strict'

	$("#dataTable").DataTable({
		"responsive": true,
		"lengthChange": false,
		"autoWidth": false,
		"ordering": true,
		select: {
            style: 'single'
        },
		"language": {
			searchPlaceholder: "Name"
		},
		"buttons": ["copy", "csv", "excel", "pdf", "print", "colvis"]
	}).buttons().container().appendTo('#dataTable_wrapper .col-md-6:eq(0)');


})