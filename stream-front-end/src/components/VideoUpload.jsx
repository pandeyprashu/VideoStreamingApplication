import React, { useState } from "react";
import axios from "axios";
import videoLogo from "../assets/uploading.png";
import {
  Button,
  Card,
  Label,
  FileInput,
  TextInput,
  Textarea,
  Progress,
  Alert,
} from "flowbite-react";

function VideoUpload() {
  const [selectedFile, setSelectedFile] = useState(false);
  const [progress, setprogress] = useState(0);
  const [meta, setMeta] = useState({
    title: "",
    description: "",
  });
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState("");

  function handleFileChange(event) {
    setSelectedFile(event.target.files[0]);
    console.log(event.target.files[0]);
  }

  function formFieldChange(event) {
    setMeta({
      ...meta,
      [event.target.name]: event.target.value,
    });
  }
  function handleForm(formEvent) {
    formEvent.preventDefault();
    if (!selectedFile) {
      alert("Select File");
      return;
    }
    // submit file to server
    saveVideoToServer(selectedFile, meta);
  }

  async function saveVideoToServer(video, VideoMetadata) {
    setUploading(true);
    // api call
    try {
      let formData = new FormData();
      formData.append("title", VideoMetadata.title);
      formData.append("description", VideoMetadata.description);
      formData.append("file", selectedFile);

      let response = await axios.post(
        "http://localhost:8080/api/v1/videos",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
          onUploadProgress: (progressEvent) => {
            const progress = Math.round(
              (progressEvent.loaded * 100) / progressEvent.total
            );
            setProgress(progress);
          },
        }
      );
      //console.log(response);
      setMessage("File Uploaded");
      setUploading(false);
    } catch (error) {
      console.error(error);
      setUploading(false);
      setMessage("File Upload Failed");
    }
  }
  return (
    <div className="text-white">
      <Card className="flex flex-col items-center justify-center">
        <h1>Upload Videos</h1>
        <div>
          <form className="flex flex-col space-y-6" onSubmit={handleForm}>
            <div>
              <div className="mb-2 block">
                <Label htmlFor="file-upload" value="Video Title" />
              </div>
              <TextInput
                onChange={formFieldChange}
                name="title"
                placeholder="Enter Title"
              />
            </div>
            <div className="max-w-md">
              <div className="mb-2 block">
                <Label htmlFor="comment" value="Video Description" />
              </div>
              <Textarea
                id="comment"
                name="description"
                onChange={formFieldChange}
                placeholder="Write Video Description..."
                required
                rows={4}
              />
            </div>
            <div className="flex items-center space-x-5 justify-center">
              <div className="shrink-0">
                <img
                  className="h-16 w-16 object-cover"
                  src={videoLogo}
                  alt="Current profile photo"
                />
              </div>
              <label className="block">
                <span className="sr-only">Choose Video File</span>
                <input
                  name="file"
                  onChange={handleFileChange}
                  type="file"
                  className="block w-full text-sm text-slate-500
        file:mr-4 file:py-2 file:px-4
        file:rounded-full file:border-0
        file:text-sm file:font-semibold
        file:bg-violet-50 file:text-violet-700
        hover:file:bg-violet-100
      "
                />
              </label>
            </div>
            <div className=" ">
             {
              uploading &&(
                <Progress
                hidden={!uploading}
                progress={progress}
                textLabel="Uploading"
                size="lg"
                labelProgress
                labelText
              />
              )}
            </div>
            <div className="">
              { message && (
                <Alert color={"success"}>
                <span className="font-medium">Submitted Successfully</span>
              </Alert>

              )}
              
            </div>
            <div className="flex justify-center">
              <Button disabled={uploading} onClick={handleForm}>Upload</Button>
            </div>
          </form>
        </div>
      </Card>
    </div>
  );
}

export default VideoUpload;
