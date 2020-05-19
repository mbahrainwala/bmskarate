import React, {useState, useEffect} from 'react';
import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  Button,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Paper
} from "@material-ui/core";
import axios from 'axios';
import {restCall} from '../utils/RestComponent'

const uploadData=new FormData();
const ListBeltVideos=(props)=>{
    const [apiRet, setApiRet] = useState({});
    const [apiListRet, setApiListRet] = useState({});

    useEffect(() => {
        if(Object.getOwnPropertyNames(apiListRet).length===0){
            restCall('GET', `${props.globalData.serverURI}/api/listTrainingVideo?belt=${props.globalData.belt.beltId}`
                , setApiListRet, props.globalData.token, null);
        }
    },[setApiListRet, apiListRet, props.globalData.belt.beltId, props.globalData.serverURI, props.globalData.token])

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
                        && error.response !== undefined
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
        <>
            {props.globalData.loginUser.type!=='U'?(
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

            </div>):null}
            <div
                style={{
                display: "flex",
                justifyContent: "center",
                margin: 20,
                padding: 20
                }}
            >
                <TableContainer component={Paper} style={{width:"80%"}}>
                    <Table aria-label="simple table">
                        <TableHead>
                          <TableRow>
                            <TableCell>Video</TableCell>
                            <TableCell>Description</TableCell>
                            <TableCell>CreatedDate</TableCell>
                            {props.globalData.loginUser.type!=='U'?(
                                <TableCell>&nbsp;</TableCell>):null}
                          </TableRow>
                        </TableHead>
                        <TableBody>
                            <>
                            {apiListRet!== undefined
                              && apiListRet!==null && apiListRet.error === undefined
                              && Object.getOwnPropertyNames(apiListRet).length !== 0?(
                                <>
                                    {apiListRet.map(video=>(
                                        <TableRow key={video.id}>
                                            <TableCell component="th" scope="row">
                                                {video.fileName}
                                            </TableCell>
                                            <TableCell scope="row">
                                                {video.description}
                                            </TableCell>
                                            <TableCell scope="row">
                                                {video.createdDate}
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </>
                            ):null}
                            </>
                        </TableBody>
                    </Table>
                </TableContainer>
            </div>
        </>
    );
}

export default ListBeltVideos