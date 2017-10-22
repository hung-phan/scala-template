"use strict";

const _ = require("lodash");
const webpack = require("webpack");
const MinifyPlugin = require("babel-minify-webpack-plugin");
const ShakePlugin = require("webpack-common-shake").Plugin;
const ExtractTextPlugin = require("extract-text-webpack-plugin");
const CompressionPlugin = require("compression-webpack-plugin");
const ManifestPlugin = require("webpack-manifest-plugin");
const OfflinePlugin = require("offline-plugin");
const productionConfig = require("./default");
const config = require(".");

_.mergeWith(
  productionConfig,
  {
    devtool: false,
    output: {
      publicPath: config.path.assets,
      filename: "[name].wp.[chunkhash].js",
      chunkFilename: "[id].wp.[chunkhash].js"
    }
  },
  (obj1, obj2) => (_.isArray(obj2) ? obj2.concat(obj1) : undefined)
);

productionConfig.module.loaders.push(  {
    test: /\.css$/,
    loader: ExtractTextPlugin.extract({
      fallback: "style-loader",
      use: `css-loader${config.cssModules}!postcss-loader`
    })
  },
  {
    test: /\.less$/,
    loader: ExtractTextPlugin.extract({
      fallback: "style-loader",
      use: `css-loader${config.cssModules}!postcss-loader!less-loader`
    })
  },
  {
    test: /\.scss$/,
    loader: ExtractTextPlugin.extract({
      fallback: "style-loader",
      use: `css-loader${config.cssModules}!postcss-loader!sass-loader`
    })
  }
);

productionConfig.plugins.unshift(
  new ShakePlugin()
);

productionConfig.plugins.push(
  new webpack.DefinePlugin({
    "process.env.NODE_ENV": "'production'"
  }),
  new ExtractTextPlugin({
    filename: "[name].wp.[contenthash].css",
    allChunks: true
  }),
  new webpack.LoaderOptionsPlugin({
    minimize: true,
    debug: false
  }),
  new OfflinePlugin({
    safeToUseOptionalCaches: true,
    caches: {
      main: ["*.js", "*.css"],
      additional: [":externals:"],
      optional: [":rest:"]
    },
    externals: ["*.woff", "*.woff2", "*.eot", "*.ttf"],
    relativePaths: false,
    ServiceWorker: {
      minify: true,
      publicPath: "/sw.js",
      events: true
    }
  }),
  new MinifyPlugin({}, {
    comments: false
  }),
  new CompressionPlugin(),
  new ManifestPlugin()
);

module.exports = productionConfig;
