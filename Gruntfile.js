module.exports = function(grunt) {

    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-watch');

    grunt.initConfig({
        copy: {
            sockjs: {
                src: 'bower_components/sockjs-client/dist/sockjs-0.3.4.min.js',
                dest: 'public/scripts/sockjs.min.js'
            },
            angular: {
                src: 'bower_components/angular/angular.min.js',
                dest: 'public/scripts/angular.min.js'
            },
            main: {
                files: [{
                    expand: true,
                    cwd: 'src/main/resources/scripts/',
                    src: '**/*.{html,js}',
                    dest: 'public/scripts/'
                }]
            }
        },
        uglify: {
            libs: {
                src: 'bower_components/vertx3-eventbus-client/vertx-eventbus.js',
                dest: 'public/scripts/vertxbus.min.js'
            }
        },
        less: {
            main: {
                files: {
                    "public/styles/main.css": "src/main/resources/styles/index.less"
                }
            }
        },
        watch: {
            styles: {
                files: ['src/main/resources/styles/**/*.less'],
                tasks: ['less:main']
            },
            scripts: {
                files: ['src/main/resources/scripts/**/*.{html,js}'],
                tasks: ['copy:main']
            }
        }
    });

    grunt.registerTask('build', ['copy', 'uglify:libs', 'less:main']);

};