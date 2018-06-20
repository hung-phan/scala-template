const ManifestPlugin = require("webpack-manifest-plugin");

module.exports = {
  type: "web-app",
  webpack: {
    config(config) {
      config.plugins.push(new ManifestPlugin());

      return config;
    }
  }
};
