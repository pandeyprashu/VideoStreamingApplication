import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import VideoUpload from "./components/VideoUpload";

function App() {
  const [count, setCount] = useState(0);
  const [videoId, setVideoId] = useState(
    "0d00c383-4d57-40fb-bc92-928935bd1df4"
  );

  return (
    <>
      <div className="flex flex-col items-center space-y-8 justify-center p-9">
        <h1 className="text-3xl font-bold text-gray-700 dark:text-gray-50">
          Video Streaming Application
        </h1>
        <div className="flex w-full justify-around">
          <div>
            <h1 className="text-white">Playing Video</h1>
            {/* <video style={{
          width: '500px',
          height: '300px',
        }} src={`http://localhost:8080/api/v1/videos/stream/${videoId}`} controls></video> */}

            <video
              id="my-video"
              class="video-js"
              controls
              preload="auto"
              width="640"
              height="400"
              poster="MY_VIDEO_POSTER.jpg"
              data-setup="{}"
            >
              <source
                src={`http://localhost:8080/api/v1/videos/stream/range/${videoId}`}
                type="video/mp4"
              />
              <p class="vjs-no-js"></p>
              <p class="vjs-no-js">
                To view this video please enable JavaScript, and consider
                upgrading to a web browser that
                <a
                  href="https://videojs.com/html5-video-support/"
                  target="_blank"
                >
                  supports HTML5 video
                </a>
              </p>
            </video>
          </div>
          <VideoUpload />
        </div>
      </div>
    </>
  );
}

export default App;
