import React, {useState} from 'react';
import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  Button,
  MenuItem,
  Select,
} from "@material-ui/core";
import axios from 'axios';

const uploadData=new FormData();
const ListBeltVideos=(props)=>{
    const [apiRet, setApiRet] = useState({});

    const changeFunc=(event)=>{
        if(event.target.id==='videodesc')
            uploadData.set('desc', event.target.value);
        if(event.target.id==='videoFile'){
            uploadData.set('file', event.target.files[0])
        }
    };

    const uploadFile=()=>{
        setApiRet({});
        if(uploadData!==null){
            uploadData.set('belt', props.globalData.belt.beltId);
            setApiRet({error:'Uploading'});
            axios.post(`${props.globalData.serverURI}/api/uploadTrainingVideo`, uploadData,
                 {headers: {
                       'Content-Type': 'multipart/form-data', 'X-SESSION-ID':props.globalData.token
                 }}
             )
             .then(response => setApiRet(response.data))
             .catch(
                error => {
                    if(error!==null && error.response!==null
                        && error.response != undefined
                        && error.response.data!==null
                        && error.response.data.message !== null
                        && error.response.data.message !== 'No message available'
                        && error.response.data.message !== ''){
                        var errorStr = {error:`${error.response.data.message}`}
                        setApiRet(errorStr);
                    }else
                        setApiRet({error:'Error Connecting to Server'});
                }
            );
        }
    }

    return(
        <div
            style={{
            display: "flex",
            justifyContent: "center",
            margin: 20,
            padding: 20
            }}
        >

        <form style={{ width: "80%" }}>
            <h1>{props.globalData.belt.beltColor} Belt Videos</h1>
            {apiRet!=null && apiRet !== undefined?(<FormLabel component="legend">{apiRet.error}</FormLabel>):(<></>)}
            {apiRet!==null && apiRet === 'Update Successful'?(<FormLabel component="legend">File Upload Successful</FormLabel>):(<></>)}
            <FormControl margin="normal" fullWidth>
                <Input type="file" id="videoFile" onChange={changeFunc}/>
            </FormControl>

            <FormControl margin="normal" fullWidth>
                <InputLabel htmlFor="videodesc">Description </InputLabel>
                <Input id="videodesc" type="text" onChange={changeFunc}/>
            </FormControl>

            <Button variant="contained" color="primary" size="medium" onClick={uploadFile}>
                Upload
            </Button>
        </form>

        </div>
    );
}

export default ListBeltVideos