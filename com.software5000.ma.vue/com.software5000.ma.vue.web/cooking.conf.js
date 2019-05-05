var path = require('path');
var cooking = require('cooking');
var build = require('./build');
var webpack = require('webpack')

var isProd = process.env.NODE_ENV === 'production';

cooking.set({
    entry : build.entries(),
    dist : '../../com.software5000.ma/src/main/webapp/web',
    template : build.templates(),
    devServer : {
        port : 8280,
        publicPath : '/web/',
        compress:true,
        clean:false,
        proxy : {
            '**' : 'http://localhost:82/'
        }
    },
    clean : false,
    hash : false,
    sourceMap : isProd ? false : true,
    minimize : true,
    chunk : {
        'chunk-vendor': {
            name: 'commons',
            filename: 'static/js/commons.js' + (isProd ? '?v=[chunkhash]' : ''),
            minChunks: 3
        }
    },
    postcss : [],
    publicPath : '/web/',
    urlLoaderLimit : 3000,
    extractCSS : isProd ? 'static/css/[name].css?v=[chunkhash]' : true,
    assetsPath : 'static/img',
    alias : {
        'src' : path.join(__dirname, 'src')
    },
    extends : ['vue2', 'buble', 'autoprefixer']
});


isProd && cooking.add('output.filename', 'static/js/[name].js?v=[chunkhash]');
module.exports = cooking.resolve();
