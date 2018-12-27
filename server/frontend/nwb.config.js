const ManifestPlugin = require("webpack-manifest-plugin");

module.exports = {
  type: "web-app",
  webpack: {
    extra: {
      plugins: [
        new ManifestPlugin()
      ]
    },
    config(config) {
      config.output.publicPath = process.env.NODE_ENV === "development"
        ? "http://localhost:8080/"
        : "/assets/";

      return config;
    }
  }
};
