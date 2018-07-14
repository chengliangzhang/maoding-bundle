/**
 * Created by Wuwq on 2016/10/20.
 */

var gulp = require('gulp');
var watch = require('gulp-watch');
var concat = require('gulp-concat');
var tmodjs = require('gulp-tmod');
var plumber = require('gulp-plumber');
var packageJSON = require('./package');
var jshintConfig = packageJSON.jshintConfig;
var jshint = require('gulp-jshint');
var uglify = require('gulp-uglify');
var cssmin = require('gulp-clean-css');
var rename = require('gulp-rename');
/*var htmlReplace = require('gulp-html-replace');*/


/*-----------   corp_server  -----------*/
//编译模板JS
var corp_server_compile_template = function () {

    return gulp.src('corp-server/src/main/resources/static/assets/module/**/*.html')
        .pipe(plumber())
        .pipe(tmodjs({
            templateBase: 'corp-server/src/main/resources/static/assets/module',
            helpers: 'corp-server/src/main/resources/static/assets/module_template/template_func.js'
        }))
        .pipe(gulp.dest('corp-server/src/main/resources/static/assets/module_template'));
};

//生成 module.js
var corp_server_generate_module_js = function () {
    return gulp.src([
        'corp-server/src/main/resources/static/assets/js/restApi.js',
        'corp-server/src/main/resources/static/assets/js/custom.js',
        'corp-server/src/main/resources/static/assets/module_template/template.js',
        'corp-server/src/main/resources/static/assets/module/**/*.js',
        'corp-server/src/main/resources/static/assets/viewJs/*.js'])
        .pipe(plumber())
        .pipe(concat('module.js'))
        .pipe(gulp.dest('corp-server/src/main/resources/static/assets/js'));
};

//生成 module.min.js
var corp_server_generate_module_min_js = function () {
    return gulp.src('corp-server/src/main/resources/static/assets/js/module.js')
        .pipe(uglify())
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest('corp-server/src/main/resources/static/assets/js'));
};

gulp.task('corp_server_generate', gulp.series(corp_server_compile_template, corp_server_generate_module_js,corp_server_generate_module_min_js));


/*-----------   web_admin  -----------*/
//编译模板JS
var web_admin_compile_template = function () {

    return gulp.src('web-admin/src/main/resources/static/assets/module/**/*.html')
        .pipe(plumber())
        .pipe(tmodjs({
            templateBase: 'web-admin/src/main/resources/static/assets/module',
            helpers: 'web-admin/src/main/resources/static/assets/module_template/template_func.js'
        }))
        .pipe(gulp.dest('web-admin/src/main/resources/static/assets/module_template'));
};

//生成 module.js
var web_admin_generate_module_js = function () {
    return gulp.src([
        'web-admin/src/main/resources/static/assets/js/module_custom.js',
        'web-admin/src/main/resources/static/assets/js/module_restApi.js',
        'web-admin/src/main/resources/static/assets/module_template/template.js',
        'web-admin/src/main/resources/static/assets/module/**/*.js',
        'web-admin/src/main/resources/static/assets/viewJs/*.js',
        'web-admin/src/main/resources/static/assets/js/module_main.js'])
        .pipe(plumber())
        .pipe(concat('module.js'))
        .pipe(gulp.dest('web-admin/src/main/resources/static/assets/js'));
};

//生成 module.min.js
var web_admin_generate_module_min_js = function () {
    return gulp.src('web-admin/src/main/resources/static/assets/js/module.js')
        .pipe(uglify())
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest('web-admin/src/main/resources/static/assets/js'));
};

//生成 module.css
var web_admin_generate_module_css = function () {
    return gulp.src(['web-admin/src/main/resources/static/assets/css/custom.css',
        'web-admin/src/main/resources/static/assets/module/**/*.css'])
        .pipe(plumber())
        .pipe(concat('module.css'))
        .pipe(gulp.dest('web-admin/src/main/resources/static/assets/css'));
};

//生成 module.min.css
var web_admin_generate_module_css_min = function () {
    return gulp.src('web-admin/src/main/resources/static/assets/css/module.css')
        .pipe(plumber())
        .pipe(cssmin())
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest('web-admin/src/main/resources/static/assets/css'));
};

gulp.task('web_admin_generate', gulp.series(web_admin_compile_template, web_admin_generate_module_js,web_admin_generate_module_min_js,web_admin_generate_module_css,web_admin_generate_module_css_min));



