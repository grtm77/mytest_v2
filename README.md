# 交通网关部署系统
<!-- Add banner here -->
![Banner](./images/1.png)

<p align="center">
  <sub>
    Built by
    <a href="https://github.com/drggboy">drggboy</a>,
    <a href="https://github.com/grtm77">grtm77</a>,
    and the community!
  </sub>
</p>

<p align="center">
  <a href="" target="_blank">
    <img alt="jdk release" src="https://img.shields.io/badge/jdk-1.8-blue">
  </a>
  <a href="" target="_blank">
    <img alt="matlab release" src="https://img.shields.io/badge/matlab-R2016b-blue">
  </a>
  </br>
  <a href="https://github.com/grtm77/mytest_v2/releases" target="_blank">
    <img alt="GitHub release" src="https://img.shields.io/github/v/release/grtm77/mytest_v2?include_prereleases&style=flat-square">
  </a>
  
  <a href="https://github.com/grtm77/mytest_v2/commits/master" target="_blank">
    <img src="https://img.shields.io/github/last-commit/grtm77/mytest_v2?style=flat-square" alt="GitHub last commit">
  </a>

  <!-- <a href="https://github.com/drggboy/mytest_v2/issues" target="_blank">
    <img src="https://img.shields.io/github/issues/drggboy/mytest_v2?style=flat-square&color=red" alt="GitHub issues">
  </a> -->

  <!-- <a href="https://github.com/drggboy/mytest_v2/pulls" target="_blank">
    <img src="https://img.shields.io/github/issues-pr/drggboy/mytest_v2?style=flat-square&color=blue" alt="GitHub pull requests">
  </a> -->

  <a href="https://github.com/grtm77/mytest_v2/graphs/contributors" target="_blank">
    <img alt="Contributors" src="https://img.shields.io/badge/all_contributors-2-orange.svg?style=flat-square">
  </a>


  <a href="https://github.com/grtm77/mytest_v2/blob/master/LICENSE" target="_blank">
    <img alt="LICENSE" src="https://img.shields.io/github/license/grtm77/mytest_v2?style=flat-square&color=yellow">
  <a/>
</p>
<hr>

<!-- Remove this note if you plan to copy this README -->


# 目录
- [交通网关部署系统](#交通网关部署系统)
- [目录](#目录)
- [演示预览](#演示预览)
- [Usage](#usage)
- [环境依赖](#环境依赖)
- [部署步骤](#部署步骤)
- [目录结构描述](#目录结构描述)
  - [xxx](#xxx)
- [Contributors](#contributors)
- [License](#license)

# 演示预览

<!-- Add a demo for your project -->

I believe that you should bring value to the reader as soon as possible. You should be able to get the user up and running with your project with minimal friction.

![Banner](./images/test.gif)

If you have a quickstart guide, this is where it should be.

Alternatively, you can add a demo to show what your project can do.

# Usage
Next, you have to explain how to use your project. You can create subsections under here to explain more clearly.


# 环境依赖
* 自行编译
  * JDK v1.8
  * MATLAB R2016b (9.1)

* 运行环境
  * JRE v1.8
  * MATLAB Runtime R2016b (9.1)



# 部署步骤

> **Note**: For longer README files, I usually add a "Back to top" buttton as shown above. It makes it easy to navigate.

Clone this repository and navigate inside the project folder and install the dependencies by running:

```sh
npm ci
```

After installing the dependencies, build the project by executing:

```sh
npm run build
```

You can use the code snippets here as well:

```shell
command to clone your project
command to build your project
command to run your project in development mode
```
# 目录结构描述
```sh
├── Readme.md                   // help
├── app                         // 应用
├── config                      // 配置
│   ├── default.json
│   ├── dev.json                // 开发环境
│   ├── experiment.json         // 实验
│   ├── index.js                // 配置控制
│   ├── local.json              // 本地
│   ├── production.json         // 生产环境
│   └── test.json               // 测试环境
├── data
├── doc                         // 文档
├── environment
├── gulpfile.js
├── locales
├── logger-service.js           // 启动日志配置
├── node_modules
├── package.json
├── app-service.js              // 启动应用配置
├── static                      // web静态资源加载
│   └── initjson
│       └── config.js         // 提供给前端的配置
├── test
├── test-service.js
└── tools
```
## xxx

To install the developer version follow the steps below. To just use the extension download from [**chrome.google.com/webstore/nsfw-filter**](https://chrome.google.com/webstore/detail/nsfw-filter/kmgagnlkckiamnenbpigfaljmanlbbhh).

To run development version in clean environment use command:

```sh
npm run dev:chrome
```

1. 添加系统环境变量
    export $PORTAL_VERSION="production" // production, test, dev


2. npm install  //安装node运行环境

3. gulp build   //前端编译

4. 启动两个配置(已forever为例)
    eg: forever start app-service.js
        forever start logger-service.js

# Contributors

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/drggboy"><img src="https://avatars.githubusercontent.com/u/47265146?v=4?s=100" width="100px;" alt="drggboy"/><br /><sub><b>drggboy</b></sub></a><br /><a href="https://github.com/grtm77/mytest_v2/commits?author=drggboy" title="Code">💻</a> <a href="https://github.com/grtm77/mytest_v2/commits?author=drggboy" title="Documentation">📖</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/grtm77"><img src="https://avatars.githubusercontent.com/u/50659884?v=4?s=100" width="100px;" alt="grtm77"/><br /><sub><b>grtm77</b></sub></a><br /><a href="https://github.com/grtm77/mytest_v2/commits?author=grtm77" title="Code">💻</a> <a href="https://github.com/grtm77/mytest_v2/commits?author=grtm77" title="Documentation">📖</a></td>
    </tr>
  </tbody>
  <tfoot>
    <tr>
      <td align="center" size="13px" colspan="7">
        <img src="https://raw.githubusercontent.com/all-contributors/all-contributors-cli/1b8533af435da9854653492b1327a23a4dbd0a10/assets/logo-small.svg">
          <a href="https://all-contributors.js.org/docs/en/bot/usage">Add your contributions</a>
        </img>
      </td>
    </tr>
  </tfoot>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!


# License
[(Back to top)](#目录)

[MIT](./LICENSE) license.