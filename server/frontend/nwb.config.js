const ManifestPlugin = require("webpack-manifest-plugin");

module.exports = {
  type: "web-app",
  webpack: {
    config(config) {
      config.plugins.push(new ManifestPlugin());

      config.output.publicPath = process.env.NODE_ENV === "development"
        ? "http://localhost:8080/"
        : "/assets/";

      return config;
    }
  }
};
