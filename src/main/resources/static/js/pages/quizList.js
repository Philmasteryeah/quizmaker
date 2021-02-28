$(function() {

	'use strict'

	$("#quizList").DataTable({
		"responsive": true,
		"lengthChange": false,
		"autoWidth": false,
		"ordering": true,
		"language": {
			searchPlaceholder: "Name"
		},
		"buttons": ["copy", "csv", "excel", "pdf", "print", "colvis"]
	}).buttons().container().appendTo('#quizList_wrapper .col-md-6:eq(0)');


})