"use strict";

const _ = require("lodash");
const config = require("./index");
const webpack = require("webpack");
const ExtractTextPlugin = require("extract-text-webpack-plugin");
const CompressionPlugin = require("compression-webpack-plugin");
const ManifestPlugin = require("webpack-manifest-plugin");
const OfflinePlugin = require("offline-plugin");
const productionConfig = require("./default");

_.mergeWith(
  productionConfig,
  {
    devtool: false,
    output: {
      publicPath: config.path.assets,
      filename: "[name].[chunkhash].js",
      chunkFilename: "[id].[chunkhash].js"
    }
  },
  (obj1, obj2) => _.isArray(obj2) ? obj2.concat(obj1) : undefined
);

productionConfig.module.loaders.push(
  {
    test: /\.css$/,
    loader: ExtractTextPlugin.extract({
      fallback: "style-loader",
      use: `css-loader${config.cssModules}!postcss-loader`
    })
  },
  {
    test: /\.less$/,
    use: ExtractTextPlugin.extract({
      fallback: "style-loader",
      use: `css-loader${config.cssModules}!postcss-loader!less-loader`
    })
  },
  {
    test: /\.scss$/,
    use: ExtractTextPlugin.extract({
      fallback: "style-loader",
      use: `css-loader${config.cssModules}!postcss-loader!sass-loader`
    })
  }
);

productionConfig.plugins.push(
  new webpack.DefinePlugin({
    "process.env.NODE_ENV": "'production'"
  }),
  new ExtractTextPlugin({
    filename: "[name].[contenthash].css",
    allChunks: true
  }),
  new webpack.LoaderOptionsPlugin({
    minimize: true,
    debug: false
  }),
  new webpack.optimize.UglifyJsPlugin({
    compress: {
      warnings: false
    },
    output: {
      comments: false
    },
    sourceMap: false
  }),
  new CompressionPlugin(),
  new OfflinePlugin({
    publicPath: config.path.assets,
    relativePaths: false,
    safeToUseOptionalCaches: true,
    externals: ["/"],
    updateStrategy: "all",
    ServiceWorker: {
      events: true,
      navigateFallbackURL: "/"
    },
    AppCache: {
      events: true,
      directory: "appcache/"
    }
  }),
  new ManifestPlugin()
);

module.exports = productionConfig;
