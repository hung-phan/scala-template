"use strict";

const _ = require("lodash");
const path = require("path");
const ROOT = require("./path-helper").ROOT;
const config = require("./index");
const webpack = require("webpack");
const developmentConfig = require("./default");

_.mergeWith(
  developmentConfig,
  {
    entry: {
      app: ["react-hot-loader/patch"]
    },
    output: {
      publicPath: `http://localhost:8080${config.path.build}`,
      filename: "[name].js",
      chunkFilename: "[id].js"
    },
    cache: true,
    devtool: "source-map",
    devServer: {
      contentBase: ROOT,
      hot: true,
      inline: true
    },
    recordsPath: path.join(ROOT, config.path.tmp, "client-records.json")
  },
  (obj1, obj2) => _.isArray(obj2) ? obj2.concat(obj1) : undefined
);

developmentConfig.module.loaders.push(
  {
    test: /\.css$/,
    loader: `style-loader!css-loader${config.cssModules}!postcss-loader`
  },
  {
    test: /\.less$/,
    loader: `style-loader!css-loader${config.cssModules}!postcss-loader!less-loader`
  },
  {
    test: /\.scss$/,
    loader: `style-loader!css-loader${config.cssModules}!postcss-loader!sass-loader`
  }
);

developmentConfig.plugins.push(
  new webpack.DefinePlugin({
    "process.env.NODE_ENV": "'development'"
  }),
  new webpack.NamedModulesPlugin(),
  new webpack.HotModuleReplacementPlugin(),
  new webpack.NoEmitOnErrorsPlugin()
);

module.exports = developmentConfig;
